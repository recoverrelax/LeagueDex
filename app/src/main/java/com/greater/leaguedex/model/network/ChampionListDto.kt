package com.greater.leaguedex.model.network

import kotlinx.serialization.Serializable

@Serializable
data class ChampionListDto(
    val data: HashMap<String, ChampionDto>
)
