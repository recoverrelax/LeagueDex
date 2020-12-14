package com.greater.leaguedex.ui.main.people

data class PeopleModel(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val language: String?,
    val vehicles: List<String>
)
