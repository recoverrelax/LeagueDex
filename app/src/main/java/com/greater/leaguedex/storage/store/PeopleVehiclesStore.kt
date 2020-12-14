package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.PeopleVehicles
import tables.PeopleVehiclesQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleVehiclesStore @Inject constructor(
    private val database: Database
) {
    private val queries: PeopleVehiclesQueries = database.peopleVehiclesQueries

    suspend fun insertAll(vehicles: List<PeopleVehicles>) = withContext(Dispatchers.IO) {
        database.transaction {
            vehicles.onEach { queries.insert(it) }
        }
    }
}
