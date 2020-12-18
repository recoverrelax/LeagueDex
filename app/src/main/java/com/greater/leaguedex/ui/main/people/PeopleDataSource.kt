package com.greater.leaguedex.ui.main.people

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import com.greater.leaguedex.storage.store.PeopleStore
import com.greater.leaguedex.storage.util.QueryType
import com.squareup.sqldelight.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.PeopleWithLanguageAndVehicles

class PeopleDataSource constructor(
    private val peopleStore: PeopleStore
) : PagingSource<String, PeopleWithLanguageAndVehicles>(), Query.Listener {
    private var query: Query<PeopleWithLanguageAndVehicles>? = null

    override fun queryResultsChanged() {
        invalidate()
    }

    override fun invalidate() {
        query?.removeListener(this)
        query = null
        super.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
        ).also {
            query?.removeListener(this)
            it.addListener(this)
            this.query = it
        }.let { withContext(Dispatchers.IO) { it.executeAsList() } }

        return LoadResult.Page(
            data = data,
            prevKey = if (params.key == null) null else data.firstOrNull()?.name,
            nextKey = data.lastOrNull()?.name
        )
    }
}
