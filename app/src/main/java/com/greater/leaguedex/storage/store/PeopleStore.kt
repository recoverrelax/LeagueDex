package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import com.greater.leaguedex.storage.data.PeopleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.People
import tables.PeopleQueries
import tables.PeopleWithLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleStore @Inject constructor(
    private val database: Database
) {
    private val queries: PeopleQueries = database.peopleQueries

    private fun List<PeopleWithLanguage>.parsePeopleEntity(): List<PeopleEntity> {
        val vehicles = queries.vehiclesAndPeople(this.map { it.id }).executeAsList()
            .groupBy(
                keySelector = { it.peopleId },
                valueTransform = { it.name }
            )

        return this.map {
            PeopleEntity(
                id = it.id,
                name = it.name,
                language = it.language,
                vehicles = vehicles[it.id] ?: emptyList()
            )
        }
    }

    suspend fun getAllWithLanguage(limit: Long, initialId: String?): List<PeopleEntity> =
        withContext(Dispatchers.IO) {
            database.transactionWithResult {
                queries.peopleWithLanguageRefreshInitial(initialId ?: "a", limit).executeAsList()
                    .parsePeopleEntity()
            }
        }

    suspend fun getAllWithLanguageAfter(id: String, limit: Long): List<PeopleEntity> =
        withContext(Dispatchers.IO) {
            database.transactionWithResult {
                queries.peopleWithLanguageAfter(id, limit).executeAsList().parsePeopleEntity()
            }
        }

    suspend fun getAllWithLanguageBefore(id: String, limit: Long): List<PeopleEntity> =
        withContext(Dispatchers.IO) {
            database.transactionWithResult {
                queries.peopleWithLanguageBefore(id, limit).executeAsList().parsePeopleEntity()
            }
        }

    suspend fun insertAll(champions: List<People>) = withContext(Dispatchers.IO) {
        database.transaction {
            champions.onEach { queries.insert(it) }
        }
    }
}
