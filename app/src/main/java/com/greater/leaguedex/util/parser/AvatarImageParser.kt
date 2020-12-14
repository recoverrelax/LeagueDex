package com.greater.leaguedex.util.parser

object AvatarImageParser {
    fun buildAvatarUrl(avatarSize: Int, name: String?): String? {
        if (name == null) return null
        // Rules:
        // one word -> first and last letter
        // multiple words -> first letter of first and last word
        val words = name.split(" ")
        val avatarLetters: String? = when {
            words.isEmpty() -> null
            words.size == 1 -> words.first().let { "${it.first()}${it.last()}" }
            else -> "${words.first().first()}${words.last().first()}"
        }
        return avatarLetters?.let { "https://eu.ui-avatars.com/api/?size=$avatarSize&name=$avatarLetters" }
    }
}
