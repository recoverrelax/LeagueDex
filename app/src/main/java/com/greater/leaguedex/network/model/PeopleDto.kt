package com.greater.leaguedex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PeopleDto(
    val name: String,
    val url: String,
    val species: List<String>,
    val vehicles: List<String>,
)
