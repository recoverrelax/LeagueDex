package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import com.greater.leaguedex.storage.data.PeopleEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.withContext
import tables.People
import tables.PeopleQueries
import tables.PeopleVehicles
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

    suspend fun getAllWithLanguage(limit: Long, initialId: String? = null): List<PeopleEntity> =
        withContext(Dispatchers.IO) {
            database.transactionWithResult {
                queries.peopleWithLanguageRefreshInitialWithKey(initialId ?: "A", limit)
                    .executeAsList().parsePeopleEntity()
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

    suspend fun insertAllPeople(data: List<People>) = withContext(Dispatchers.IO) {
        database.transaction {
            data.onEach { queries.insertPeople(it) }
        }
    }

    suspend fun insertAllPeopleVehicles(data: List<PeopleVehicles>) = withContext(Dispatchers.IO) {
        database.transaction {
            data.onEach { queries.insertPeopleVehicles(it) }
        }
    }

    @Suppress("RedundantUnitExpression")
    fun observePeopleAndVehiclesChanges(): Flow<Unit> {
        return combine(
            queries.existsPeople().asFlow().mapToOne(),
            queries.existsPeopleVehicles().asFlow().mapToOne()
        ) { _, _ -> Unit }.drop(1)
    }
}
