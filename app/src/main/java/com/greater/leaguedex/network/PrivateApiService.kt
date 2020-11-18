package com.greater.leaguedex.network

import com.greater.leaguedex.model.network.ChampionListDto
import retrofit2.http.GET

interface PrivateApiService{
    @GET("https://ddragon.leagueoflegends.com/cdn/10.23.1/data/en_US/champion.json")
    suspend fun getChampionList(): ChampionListDto
}