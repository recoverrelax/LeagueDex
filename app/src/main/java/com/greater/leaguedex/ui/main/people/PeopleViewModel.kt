package com.greater.leaguedex.ui.main.people

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.greater.leaguedex.mvvm.BaseViewModel
import com.greater.leaguedex.storage.store.PeopleStore
import com.greater.leaguedex.sync.DataSync
import com.greater.leaguedex.util.UpdateStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class PeopleViewModel @ViewModelInject constructor(
    private val peopleStore: PeopleStore,
    private val dataSync: DataSync,
    dataSourceFactory: PeopleDataSourceFactory,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<PeopleViewStates>() {

    private val STATE_KEY = "PeopleViewModel.STATE_KEY"

    private val peopleList: Flow<PagedList<PeopleItem>> = dataSourceFactory.createAsFlow(
        config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()
    )
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
        .filterNotNull()

    private val refresher = MutableSharedFlow<Boolean>()

    @Suppress("RedundantUnitExpression")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun initialize() {
        viewScope.launch {
            refresher
                .flatMapLatest { syncNeeded ->
                    Timber.i("flatMapLatest: $syncNeeded")
                    dataSync.sync(syncNeeded)
                        .map { status ->
                            PeopleViewStates.LoadingStatus(
                                isLoading = status == UpdateStatus.STARTED,
                                syncError = status == UpdateStatus.ERROR
                            )
                        }
                }
                .onEach { postEvent(it) }
                .launchIn(this)

            refresher.emit(dataSync.isSyncNeeded())

            peopleList
                .map { PeopleViewStates.UpdateList(data = it) }
                .collectLatest { postEvent(it) }
        }
    }

    fun saveState(firstVisibleKey: String) {
        savedStateHandle.set(STATE_KEY, firstVisibleKey)
    }

    fun onRefreshRequested() {
        viewScope.launch { refresher.emit(true) }
    }

    fun onFavouriteClicked(itemId: Long, isFavourite: Boolean) {
        viewModelScope.launch { peopleStore.updateFavourite(itemId, isFavourite.not()) }
    }
}
