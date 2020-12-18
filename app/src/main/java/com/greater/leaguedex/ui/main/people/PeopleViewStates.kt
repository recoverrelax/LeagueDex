package com.greater.leaguedex.ui.main.people

import androidx.paging.PagingData
import com.greater.leaguedex.mvvm.BaseViewStates

sealed class PeopleViewStates : BaseViewStates {
    data class UpdateList(val data: PagingData<PeopleItem>) : PeopleViewStates()
    data class RequestSwipeRefresh(val refreshing: Boolean, val requestAdapterRefresh: Boolean = false) : PeopleViewStates()
    object ShowSyncError : PeopleViewStates()
}
