package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import com.greater.leaguedex.storage.util.QueryType
import com.greater.leaguedex.storage.util.toSqlOrder
import com.squareup.sqldelight.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.PeopleEntity
import tables.PeopleFavouritesEntity
import tables.PeopleQueries
import tables.PeopleVehicles
import tables.PeopleWithLanguageAndVehicles
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleStore @Inject constructor(
    private val database: Database
) {
    private val queries: PeopleQueries = database.peopleQueries

    fun getAllWithLanguage(queryType: QueryType, query: String?, limit: Long): Query<PeopleWithLanguageAndVehicles> =
        queries.peopleWithLanguage(queryType.toSqlOrder(), query ?: "", limit)

    suspend fun insertAllPeople(data: List<PeopleEntity>) = withContext(Dispatchers.IO) {
        database.transaction {
            data.onEach { queries.insertPeople(it) }
        }
    }

    suspend fun updateFavourite(peopleId: Long, isFavourite: Boolean) = withContext(Dispatchers.IO) {
        queries.insertOrReplaceFavourite(PeopleFavouritesEntity(peopleId, isFavourite))
    }

    suspend fun insertAllPeopleVehicles(data: List<PeopleVehicles>) = withContext(Dispatchers.IO) {
        database.transaction {
            data.onEach { queries.insertPeopleVehicles(it) }
        }
    }
}
