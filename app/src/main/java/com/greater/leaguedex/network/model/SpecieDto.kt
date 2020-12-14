package com.greater.leaguedex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SpecieDto(
    val name: String,
    val language: String,
    val url: String
)
