package com.greater.leaguedex.ui.championlist

import com.greater.leaguedex.composeble.component.ChampionRowModel
import com.greater.leaguedex.mvvm.BaseViewStates

sealed class ChampionListState : BaseViewStates {
    data class UpdateItemList(val itemList: List<ChampionRowModel>) : ChampionListState()
}
