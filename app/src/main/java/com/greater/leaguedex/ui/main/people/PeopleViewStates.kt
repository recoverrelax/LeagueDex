package com.greater.leaguedex.ui.main.people

import androidx.paging.PagingData
import com.greater.leaguedex.mvvm.BaseViewStates

sealed class PeopleViewStates : BaseViewStates {
    data class UpdateList(val data: PagingData<PeopleModel>) : PeopleViewStates()
    data class RequestSwipeRefresh(val refreshing: Boolean) : PeopleViewStates()
    object ShowSyncError : PeopleViewStates()
}
