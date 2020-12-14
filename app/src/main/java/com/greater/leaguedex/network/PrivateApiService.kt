package com.greater.leaguedex.network

import com.greater.leaguedex.network.model.PeopleListDto
import com.greater.leaguedex.network.model.SpecieListDto
import com.greater.leaguedex.network.model.VehicleListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PrivateApiService {
    @GET("people/")
    suspend fun getPeople(
        @Query("page") page: Int
    ): PeopleListDto

    @GET("species/")
    suspend fun getSpecies(
        @Query("page") page: Int
    ): SpecieListDto

    @GET("vehicles/")
    suspend fun getVehicles(
        @Query("page") page: Int
    ): VehicleListDto
}
