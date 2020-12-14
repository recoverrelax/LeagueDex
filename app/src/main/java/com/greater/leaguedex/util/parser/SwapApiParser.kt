package com.greater.leaguedex.util.parser

enum class IdType(val resolvedName: String) {
    PEOPLE("people"),
    SPECIES("species"),
    VEHICLES("vehicles"),
}

object SwapApiParser {
    fun parseIdFromUrl(url: String, type: IdType): Long {
        return url.substringAfter("${type.resolvedName}/").dropLast(1).toLong()
    }

    fun parseNextPage(url: String): Int {
        return url.substringAfterLast("=").toInt()
    }
}
