package com.greater.leaguedex.storage.store

import com.greater.leaguedex.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.SettingsEntity
import tables.SettingsQueries
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingStore @Inject constructor(
    database: Database
) {
    private val queries: SettingsQueries = database.settingsQueries

    companion object {
        const val SETTING_TABLE_ID = 0L
    }

    suspend fun insert(
        lastRefresh: Long
    ) = withContext(Dispatchers.IO) {
        queries.insert(SettingsEntity(SETTING_TABLE_ID, lastRefresh))
    }

    suspend fun getRefreshInfo(): SettingsEntity? = withContext(Dispatchers.IO) {
        queries.refreshInfo().executeAsOneOrNull()
    }
}
