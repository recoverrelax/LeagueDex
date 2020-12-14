package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.Specie
import tables.SpecieQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpecieStore @Inject constructor(
    private val database: Database
) {
    private val queries: SpecieQueries = database.specieQueries

    suspend fun getAll(): List<Specie> = withContext(Dispatchers.IO) {
        queries.getAll().executeAsList()
    }

    suspend fun insertAll(champions: List<Specie>) = withContext(Dispatchers.IO) {
        database.transaction {
            champions.onEach { queries.insert(it) }
        }
    }

    suspend fun exists(): Boolean = withContext(Dispatchers.IO) {
        queries.exists().executeAsOne()
    }
}
