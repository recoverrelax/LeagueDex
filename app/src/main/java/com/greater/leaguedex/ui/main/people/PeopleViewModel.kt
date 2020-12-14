package com.greater.leaguedex.ui.main.people

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.greater.leaguedex.mvvm.BaseViewModel
import com.greater.leaguedex.storage.data.PeopleEntity
import com.greater.leaguedex.ui.main.app.DataSync
import com.greater.leaguedex.util.UpdateStatus
import com.greater.leaguedex.util.parser.AvatarImageParser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Provider

class PeopleViewModel @ViewModelInject constructor(
    peopleDataSource: Provider<PeopleDataSource>,
    private val dataSync: DataSync,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<PeopleViewStates>() {
    private val STATE_KEY = "PeopleViewModel.STATE_KEY"

    private val peopleList = Pager(
        initialKey = savedStateHandle.get(STATE_KEY),
        config = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) { peopleDataSource.get() }
        .flow
        .map { pagingData -> pagingData.map { mapToModel(it) } }
        .cachedIn(viewModelScope)

    private val swipeRefresh = Channel<Unit>(capacity = 1)

    fun initialize() {
        viewModelScope.launch {
            swipeRefresh.consumeAsFlow()
                .onStart { emit(Unit) }
                .flatMapLatest { dataSync.sync() }
                .mapNotNull {
                    when (it) {
                        UpdateStatus.FINISHED -> PeopleViewStates.RequestSwipeRefresh(false)
                        UpdateStatus.ERROR -> PeopleViewStates.ShowSyncError
                        UpdateStatus.STARTED -> PeopleViewStates.RequestSwipeRefresh(true)
                    }
                }
                .onEach { postEvent(it) }
                .launchIn(viewModelScope)

            peopleList
                .map { PeopleViewStates.UpdateList(data = it) }
                .collectLatest { postEvent(it) }
        }
    }

    private fun mapToModel(people: PeopleEntity): PeopleModel {
        return PeopleModel(
            id = people.id,
            name = people.name,
            imageUrl = AvatarImageParser.buildAvatarUrl(
                avatarSize = 50,
                name = people.language
            ),
            language = people.language ?: "na",
            vehicles = people.vehicles
        )
    }

    fun saveState(firstVisibleKey: String) {
        savedStateHandle.set(STATE_KEY, firstVisibleKey)
    }

    fun onRefreshRequested() {
        swipeRefresh.offer(Unit)
    }
}
