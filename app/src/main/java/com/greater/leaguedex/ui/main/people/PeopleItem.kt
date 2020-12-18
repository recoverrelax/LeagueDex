package com.greater.leaguedex.ui.main.people

data class PeopleItem(
    val id: Long,
    val name: String,
    val isFavourite: Boolean,
    val imageUrl: String?,
    val language: String?,
    val vehicles: String
) {
    fun isFavouritesOnlyDifferent(other: PeopleItem): Boolean {
        return this.id == other.id && this.name == other.name && this.imageUrl == other.imageUrl &&
            this.language == other.language && this.vehicles == other.vehicles &&
            this.isFavourite != other.isFavourite
    }
}
