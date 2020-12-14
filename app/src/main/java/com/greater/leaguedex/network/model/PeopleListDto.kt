package com.greater.leaguedex.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PeopleListDto(
    val next: String?,
    val results: List<PeopleDto>
)
