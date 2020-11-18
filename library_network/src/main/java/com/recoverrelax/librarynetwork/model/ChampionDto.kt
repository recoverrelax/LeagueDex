package com.recoverrelax.librarynetwork.model

import kotlinx.serialization.Serializable

@Serializable
data class ChampionDto(
    val version: String,
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val blurb: String,
    val image: ChampionImage
)
