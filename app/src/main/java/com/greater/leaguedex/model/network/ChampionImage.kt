package com.greater.leaguedex.model.network

import kotlinx.serialization.Serializable

@Serializable
data class ChampionImage(
    val full: String,
    val sprite: String
)