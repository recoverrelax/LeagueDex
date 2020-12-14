package com.greater.leaguedex.ui.main.people

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.greater.leaguedex.mvvm.BaseViewModel
import com.greater.leaguedex.storage.data.PeopleEntity
import com.greater.leaguedex.util.parser.AvatarImageParser
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import tables.PeopleWithLanguage
import timber.log.Timber
import javax.inject.Provider

class PeopleViewModel @ViewModelInject constructor(
    peopleDataSource: Provider<PeopleDataSource>,
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

    fun onStart() {
        peopleList
            .onEach {
                postEvent(PeopleViewStates.UpdateList(data = it))
            }
            .launchIn(viewModelScope)
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
}