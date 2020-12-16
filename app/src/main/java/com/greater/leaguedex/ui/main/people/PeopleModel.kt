package com.greater.leaguedex.ui.main.people

data class PeopleModel(
    val id: Long,
    val name: String,
    var isFavourite: Boolean,
    val imageUrl: String?,
    val language: String?,
    val vehicles: String
)
