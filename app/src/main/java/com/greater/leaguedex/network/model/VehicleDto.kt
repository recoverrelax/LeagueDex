package com.greater.leaguedex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleDto(
    val name: String,
    val url: String
)