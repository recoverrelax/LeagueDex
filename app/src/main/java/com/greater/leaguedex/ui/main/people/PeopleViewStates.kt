package com.greater.leaguedex.ui.main.people

import androidx.paging.PagedList
import com.greater.leaguedex.mvvm.BaseViewStates

sealed class PeopleViewStates : BaseViewStates {
    data class UpdateList(val data: PagedList<PeopleItem>) : PeopleViewStates()
    data class LoadingStatus(
        val isLoading: Boolean,
        val syncError: Boolean
    ) : PeopleViewStates()
}
