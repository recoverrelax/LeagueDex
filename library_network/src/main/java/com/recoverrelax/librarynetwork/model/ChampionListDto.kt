package com.recoverrelax.librarynetwork.model

import kotlinx.serialization.Serializable

@Serializable
data class ChampionListDto(
    val data: HashMap<String, ChampionDto>
)
