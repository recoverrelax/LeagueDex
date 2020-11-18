package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import com.greater.leaguedex.storage.OrderBy
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tables.Champion
import tables.ChampionQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionStore @Inject constructor(
    private val database: Database
) {
    private val queries: ChampionQueries = database.championQueries

    suspend fun getAll(): Champion = withContext(Dispatchers.IO) {
        queries.getAll().executeAsOne()
    }

    suspend fun insertAll(champions: List<Champion>) = withContext(Dispatchers.IO) {
        database.transaction {
            champions.onEach { queries.insert(it) }
        }
    }

    suspend fun exists(): Boolean = withContext(Dispatchers.IO) {
        queries.exists().executeAsOne()
    }

    fun observeAll(order: OrderBy = OrderBy.NONE): Flow<List<Champion>> {
        return when (order) {
            OrderBy.NONE -> queries.getAll()
            OrderBy.BY_NAME -> queries.getAllOrderByName()
        }.asFlow().mapToList(Dispatchers.IO)
    }
}
