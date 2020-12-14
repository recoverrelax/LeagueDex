package com.greater.leaguedex.storage.data

data class PeopleEntity(
    val id: Long,
    val name: String,
    val language: String?,
    val vehicles: List<String>
)
