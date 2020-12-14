package com.greater.leaguedex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleListDto(
    val next: String?,
    val results: List<VehicleDto>
)
