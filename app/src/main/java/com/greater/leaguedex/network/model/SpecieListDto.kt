package com.greater.leaguedex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SpecieListDto(
    val next: String?,
    val results: List<SpecieDto>
)
