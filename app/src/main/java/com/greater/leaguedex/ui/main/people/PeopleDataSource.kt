package com.greater.leaguedex.ui.main.people

import androidx.paging.PagingSource
import com.greater.leaguedex.storage.data.PeopleEntity
import com.greater.leaguedex.storage.store.PeopleStore
import timber.log.Timber
import javax.inject.Inject

class PeopleDataSource @Inject constructor(
    private val peopleStore: PeopleStore
) : PagingSource<String, PeopleEntity>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, PeopleEntity> {
        val data = when (params) {
            is LoadParams.Refresh -> peopleStore.getAllWithLanguage(
                params.loadSize.toLong(),
                params.key
            )
            is LoadParams.Append -> peopleStore.getAllWithLanguageAfter(
                params.key,
                params.loadSize.toLong()
            )
            is LoadParams.Prepend -> peopleStore.getAllWithLanguageBefore(
                params.key,
                params.loadSize.toLong()
            )
        }

        Timber.i(
            """
                type: ${params.javaClass.simpleName},
                key: ${params.key},
                dataSize: ${data.size},
                data: ${data.map { it.name }.joinToString(", ")}
            """.trimIndent()
        )

        return LoadResult.Page(
            data = data,
            prevKey = if (params.key == null) null else data.firstOrNull()?.name,
            nextKey = data.lastOrNull()?.name
        )
    }
}
