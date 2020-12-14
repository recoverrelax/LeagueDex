package com.greater.leaguedex.ui.main.people

import androidx.paging.PagingSource
import com.greater.leaguedex.storage.data.PeopleEntity
import com.greater.leaguedex.storage.store.PeopleStore
import javax.inject.Inject

class PeopleDataSource @Inject constructor(
    private val peopleStore: PeopleStore
): PagingSource<String, PeopleEntity>() {
    var currentKey: String? = null
    private set

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PeopleEntity> {
        val data = when(params){
            is LoadParams.Refresh -> peopleStore.getAllWithLanguage(params.loadSize.toLong(), params.key)
            is LoadParams.Append -> peopleStore.getAllWithLanguageAfter(params.key, params.loadSize.toLong())
            is LoadParams.Prepend -> peopleStore.getAllWithLanguageBefore(params.key, params.loadSize.toLong())
        }
        this.currentKey = params.key
        return LoadResult.Page(
            data = data,
            prevKey = if(params.key == null) null else data.firstOrNull()?.name,
            nextKey = if(data.size < params.loadSize) null else data.lastOrNull()?.name
        )
    }
}