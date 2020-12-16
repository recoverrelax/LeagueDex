package com.greater.leaguedex.usecase

import com.greater.leaguedex.network.PrivateApiService
import com.greater.leaguedex.network.model.VehicleDto
import com.greater.leaguedex.storage.store.VehiclesStore
import com.greater.leaguedex.util.parser.IdType
import com.greater.leaguedex.util.parser.SwapApiParser
import tables.VehicleEntity
import javax.inject.Inject

class FetchAndUpdateVehicles @Inject constructor(
    private val apiService: PrivateApiService,
    private val vehiclesStore: VehiclesStore
) {

    suspend operator fun invoke(initialPage: Int = 1) {
        val vehiclesPages = apiService.getVehicles(initialPage)
        storeVehicles(vehiclesPages.results)
        if (vehiclesPages.next != null) {
            // no more pages
            invoke(SwapApiParser.parseNextPage(vehiclesPages.next))
        }
    }

    private suspend fun storeVehicles(results: List<VehicleDto>) {
        vehiclesStore.insertAll(
            results.map { vehicle ->
                VehicleEntity(
                    id = SwapApiParser.parseIdFromUrl(vehicle.url, IdType.VEHICLES),
                    name = vehicle.name
                )
            }
        )
    }
}
