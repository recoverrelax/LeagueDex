package com.recoverrelax.librarynetwork.model

import kotlinx.serialization.Serializable

@Serializable
data class ChampionImage(
    val full: String,
    val sprite: String
)
