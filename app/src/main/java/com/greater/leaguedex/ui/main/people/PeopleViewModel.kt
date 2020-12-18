package com.greater.leaguedex.ui.main.people

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.greater.leaguedex.mvvm.BaseViewModel
import com.greater.leaguedex.storage.store.PeopleStore
import com.greater.leaguedex.ui.main.app.DataSync
import com.greater.leaguedex.util.UpdateStatus
import com.greater.leaguedex.util.parser.AvatarImageParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import tables.PeopleWithLanguageAndVehicles

class PeopleViewModel @ViewModelInject constructor(
    private val peopleStore: PeopleStore,
    private val dataSync: DataSync,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<PeopleViewStates>() {

    private val STATE_KEY = "PeopleViewModel.STATE_KEY"

    private val peopleList: Flow<PagingData<PeopleItem>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) { PeopleDataSource(peopleStore) }
        .flow
        .map { pagingData -> pagingData.map { mapToModel(it) } }
        .cachedIn(viewModelScope)

    private val swipeRefresh = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun initialize() {
        viewScope.launch {
            swipeRefresh
                .onStart { emit(Unit) }
                .flatMapLatest { dataSync.sync(forceRefresh = false) }
                .mapNotNull(::mapToViewStates)
                .onEach { postEvent(it) }
                .launchIn(this)

            peopleList
                .map { PeopleViewStates.UpdateList(data = it) }
                .collectLatest { postEvent(it) }
        }
    }

    private fun mapToViewStates(syncState: UpdateStatus): PeopleViewStates {
        return when (syncState) {
            UpdateStatus.FINISHED -> PeopleViewStates.RequestSwipeRefresh(
                refreshing = false,
                requestAdapterRefresh = true
            )
            UpdateStatus.ERROR -> PeopleViewStates.ShowSyncError
            UpdateStatus.STARTED -> PeopleViewStates.RequestSwipeRefresh(true)
            UpdateStatus.NONE -> PeopleViewStates.RequestSwipeRefresh(false)
        }
    }

    private fun mapToModel(people: PeopleWithLanguageAndVehicles): PeopleItem {
        return PeopleItem(
            id = people.id,
            name = people.name,
            imageUrl = AvatarImageParser.buildAvatarUrl(
                avatarSize = 50,
                name = people.language
            ),
            language = people.language ?: "na",
            vehicles = people.vehicles,
            isFavourite = people.isFavourite ?: false
        )
    }

    fun saveState(firstVisibleKey: String) {
        savedStateHandle.set(STATE_KEY, firstVisibleKey)
    }

    fun onRefreshRequested() {
        viewModelScope.launch { swipeRefresh.emit(Unit) }
    }

    fun onFavouriteClicked(itemId: Long, isFavourite: Boolean) {
        viewModelScope.launch { peopleStore.updateFavourite(itemId, isFavourite.not()) }
    }
}
