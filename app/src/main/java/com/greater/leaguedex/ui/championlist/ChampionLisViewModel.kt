package com.greater.leaguedex.ui.championlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.greater.leaguedex.mvvm.BaseViewModel
import com.greater.leaguedex.storage.OrderBy
import com.greater.leaguedex.storage.store.ChampionStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import tables.Champion

class ChampionLisViewModel @ViewModelInject constructor(
    championStore: ChampionStore
) : BaseViewModel<ChampionListState>() {

    private val championList: StateFlow<List<Champion>?> =
        championStore.observeAll(order = OrderBy.BY_NAME)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onStart() {
        championList
            .filterNotNull()
            .onEach { postEvent(ChampionListState.UpdateItemList(it)) }
            .launchIn(viewModelScope)
    }
}