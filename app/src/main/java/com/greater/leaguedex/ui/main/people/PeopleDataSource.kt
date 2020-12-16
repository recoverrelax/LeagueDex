package com.greater.leaguedex.ui.main.people

import androidx.paging.PagingSource
import com.greater.leaguedex.storage.store.PeopleStore
import com.greater.leaguedex.storage.util.QueryType
import tables.PeopleWithLanguageAndVehicles
import javax.inject.Inject

class PeopleDataSource @Inject constructor(
    private val peopleStore: PeopleStore
) : PagingSource<String, PeopleWithLanguageAndVehicles>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, PeopleWithLanguageAndVehicles> {
        // we could use limit offset based query
        // this technique is faster (aka scrolling cursor)
        // but way more pain to implement the queries

        val data = peopleStore.getAllWithLanguage(
            queryType = when (params) {
                is LoadParams.Refresh -> QueryType.INITIAL
                is LoadParams.Append -> QueryType.APPEND
                is LoadParams.Prepend -> QueryType.PREPEND
            },
            query = params.key,
            params.loadSize.toLong()
        )

        return LoadResult.Page(
            data = data,
            prevKey = if (params.key == null) null else data.firstOrNull()?.name,
            nextKey = data.lastOrNull()?.name
        )
    }
}
