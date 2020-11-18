package com.greater.leaguedex.usecase

import com.greater.leaguedex.storage.store.ChampionStore
import com.recoverrelax.librarynetwork.PrivateApiService
import com.recoverrelax.librarynetwork.model.ChampionImage
import com.recoverrelax.librarynetwork.model.ChampionListDto
import tables.Champion
import javax.inject.Inject

class FetchUpdateChampionUseCase @Inject constructor(
    private val apiService: PrivateApiService,
    private val championStore: ChampionStore
) {
    private val CHAMP_IMAGE_URL = "https://ddragon.leagueoflegends.com/cdn/10.23.1/img/champion/"

    suspend fun run() {
        if (championStore.exists()) return

        val champions = apiService.getChampionList()
            .convertToDb()

        championStore.insertAll(champions)
    }

    private fun ChampionListDto.convertToDb(): List<Champion> {
        return this.data.values.map {
            Champion(
                id = it.key.toLong(),
                name = it.name,
                imageIcon = parseImageIcon(it.image),
                title = it.title,
                description = it.blurb
            )
        }
    }

    private fun parseImageIcon(championName: ChampionImage): String {
        return "${CHAMP_IMAGE_URL}${championName.full}"
    }
}
