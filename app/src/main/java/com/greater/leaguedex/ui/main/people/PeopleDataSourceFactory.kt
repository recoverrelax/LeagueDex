package com.greater.leaguedex.ui.main.people

import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.greater.leaguedex.storage.store.PeopleStore
import com.greater.leaguedex.storage.util.QueryType
import com.greater.leaguedex.util.coroutines.FlowPagedListBuilder
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.android.paging.QueryDataSourceFactory
import kotlinx.coroutines.flow.Flow
import tables.PeopleWithLanguageAndVehicles
import javax.inject.Inject

class PeopleDataSourceFactory @Inject constructor(
    peopleStore: PeopleStore
) {

    private class PeopleItemDataSource(
        private val peopleStore: PeopleStore
    ) :
        ItemKeyedDataSource<String, PeopleWithLanguageAndVehicles>(), Query.Listener {
        private var query: Query<PeopleWithLanguageAndVehicles>? = null

        override fun queryResultsChanged() = invalidate()

        override fun getKey(item: PeopleWithLanguageAndVehicles): String {
            return item.name
        }

        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<PeopleWithLanguageAndVehicles>
        ) = load(params.requestedInitialKey, params.requestedLoadSize, QueryType.INITIAL, callback)

        override fun loadAfter(
            params: LoadParams<String>,
            callback: LoadCallback<PeopleWithLanguageAndVehicles>
        ) = load(params.key, params.requestedLoadSize, QueryType.APPEND, callback)

        override fun loadBefore(
            params: LoadParams<String>,
            callback: LoadCallback<PeopleWithLanguageAndVehicles>
        ) = load(params.key, params.requestedLoadSize, QueryType.PREPEND, callback)

        private fun load(
            key: String?,
            limit: Int,
            queryType: QueryType,
            callback: LoadCallback<PeopleWithLanguageAndVehicles>
        ) {
            query?.removeListener(this)
            peopleStore.getAllWithLanguage(
                queryType = queryType,
                query = key,
                limit = limit.toLong()
            ).let { query ->
                query.addListener(this)
                this.query = query
                if (!isInvalid) {
                    callback.onResult(query.executeAsList())
                }
            }
        }
    }

    private val scrollingCursorFactory: DataSource.Factory<String, PeopleWithLanguageAndVehicles> =
        object : DataSource.Factory<String, PeopleWithLanguageAndVehicles>() {
            override fun create(): DataSource<String, PeopleWithLanguageAndVehicles> {
                return PeopleItemDataSource(peopleStore)
            }
        }

    private val limitOffsetFactory = QueryDataSourceFactory(
        queryProvider = peopleStore::getPeopleWithLanguageWithLimitOffset,
        countQuery = peopleStore.countPeopleWithLanguage(),
        transacter = peopleStore
    )

    fun createAsFlow(
        config: PagedList.Config
    ): Flow<PagedList<PeopleItem>> {
        val factory = limitOffsetFactory
            .map { mapToModel(it) }

        return FlowPagedListBuilder(
            dataSourceFactory = factory,
            config = config,
            boundaryCallback = null
        ).buildFlow()
    }

    private fun mapToModel(people: PeopleWithLanguageAndVehicles): PeopleItem {
        return PeopleItem(
            id = people.id,
            name = people.name,
            language = people.language,
            vehicles = people.vehicles,
            isFavourite = people.isFavourite ?: false
        )
    }
}
