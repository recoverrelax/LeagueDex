package com.greater.leaguedex.ui.championlist

import com.greater.leaguedex.mvvm.BaseViewStates
import tables.Champion

sealed class ChampionListState : BaseViewStates {
    data class UpdateItemList(val itemList: List<Champion>) : ChampionListState()
}
