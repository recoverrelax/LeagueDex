package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import com.greater.leaguedex.storage.util.QueryType
import com.greater.leaguedex.storage.util.toSqlOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.PeopleEntity
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

    suspend fun getAllWithLanguage(queryType: QueryType, query: String?, limit: Long): List<PeopleWithLanguageAndVehicles> =
        withContext(Dispatchers.IO) {
            queries.peopleWithLanguage(queryType.toSqlOrder(), query ?: "", limit)
                .executeAsList()
        }

    suspend fun insertAllPeople(data: List<PeopleEntity>) = withContext(Dispatchers.IO) {
        database.transaction {
            data.onEach { queries.insertPeople(it) }
        }
    }

    suspend fun updateFavourite(peopleId: Long, isFavourite: Boolean) = withContext(Dispatchers.IO) {
        queries.updateFavourite(isFavourite, peopleId)
    }

    suspend fun insertAllPeopleVehicles(data: List<PeopleVehicles>) = withContext(Dispatchers.IO) {
        database.transaction {
            data.onEach { queries.insertPeopleVehicles(it) }
        }
    }
}
