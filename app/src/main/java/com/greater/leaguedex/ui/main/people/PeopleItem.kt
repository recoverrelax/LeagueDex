package com.greater.leaguedex.ui.main.people

import com.greater.leaguedex.util.parser.AvatarImageParser
import timber.log.Timber

data class PeopleItem(
    val id: Long,
    val name: String,
    val isFavourite: Boolean,
    private val language: String?,
    val vehicles: String
) {

    val spokenLanguage: String = this.language ?: "none"

    fun isFavouritesOnlyDifferent(other: PeopleItem): Boolean {
        return this.id == other.id && this.name == other.name && this.language == other.language &&
            this.language == other.language && this.vehicles == other.vehicles &&
            this.isFavourite != other.isFavourite
    }

    fun imageUrl(size: Int): String? {
        return AvatarImageParser.buildAvatarUrl(
            avatarSize = size,
            name = this.language
        ).also { Timber.i("ImageUrl: $it") }
    }
}
