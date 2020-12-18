package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.VehicleEntity
import tables.VehicleQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehiclesStore @Inject constructor(
    private val database: Database
) {
    private val queries: VehicleQueries = database.vehicleQueries

    suspend fun insertAll(vehicles: List<VehicleEntity>) = withContext(Dispatchers.IO) {
        database.transaction {
            vehicles.onEach { queries.insert(it) }
        }
    }
}
