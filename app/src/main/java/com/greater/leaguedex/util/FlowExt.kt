package com.greater.leaguedex.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T : Any?> Flow<List<T>>.flatten(): Flow<T> = flow {
    collect { list ->
        list.onEach { element -> emit(element) }
    }
}
