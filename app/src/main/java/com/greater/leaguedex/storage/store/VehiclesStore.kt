package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.Vehicle
import tables.VehicleQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehiclesStore @Inject constructor(
    private val database: Database
) {
    private val queries: VehicleQueries = database.vehicleQueries

    suspend fun getAllById(vehicleId: Long): List<Vehicle> = withContext(Dispatchers.IO) {
        queries.getAllByID(vehicleId).executeAsList()
    }

    suspend fun insertAll(vehicles: List<Vehicle>) = withContext(Dispatchers.IO) {
        database.transaction {
            vehicles.onEach { queries.insert(it) }
        }
    }
}
