package com.greater.leaguedex.storage.util

enum class QueryType {
    INITIAL,
    APPEND,
    PREPEND
}

fun QueryType.toSqlOrder(): Long {
    return when (this) {
        QueryType.INITIAL -> 0
        QueryType.APPEND -> 1
        QueryType.PREPEND -> -1
    }
}
