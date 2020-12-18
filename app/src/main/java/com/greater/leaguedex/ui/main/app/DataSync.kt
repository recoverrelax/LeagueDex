package com.greater.leaguedex.ui.main.app

import com.greater.leaguedex.storage.store.SettingStore
import com.greater.leaguedex.usecase.FetchAndUpdatePeople
import com.greater.leaguedex.usecase.FetchAndUpdateSpecies
import com.greater.leaguedex.usecase.FetchAndUpdateVehicles
import com.greater.leaguedex.util.UpdateStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tables.SettingsEntity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataSync @Inject constructor(
    private val fetchAndUpdatePeople: FetchAndUpdatePeople,
    private val fetchAndUpdateSpecie: FetchAndUpdateSpecies,
    private val fetchAndUpdateVehicles: FetchAndUpdateVehicles,
    private val settingStore: SettingStore
) {
    private val REFRESH_TIMEOUT = TimeUnit.HOURS.toMillis(1)

    private suspend fun isSyncNeeded(): Boolean {
        val refreshInfo = settingStore.getRefreshInfo()
        return refreshInfo.shouldUpdate()
    }

    fun sync(forceRefresh: Boolean = false): Flow<UpdateStatus> {
        return flow {
            if (!forceRefresh && isSyncNeeded().not()) {
                emit(UpdateStatus.NONE)
            } else {
                emit(UpdateStatus.STARTED)

                runCatching {
                    fetchAndUpdatePeople()
                    fetchAndUpdateSpecie()
                    fetchAndUpdateVehicles()
                }.fold(
                    onSuccess = {
                        updateSyncStatusSuccess()
                        emit(UpdateStatus.FINISHED)
                    },
                    onFailure = {
                        emit(UpdateStatus.ERROR)
                    }
                )
            }
        }
    }

    private suspend fun updateSyncStatusSuccess() {
        settingStore.insert(
            lastRefresh = System.currentTimeMillis()
        )
    }

    private fun SettingsEntity?.shouldUpdate(): Boolean {
        if (this == null) return true
        return this.lastRefresh + REFRESH_TIMEOUT <= System.currentTimeMillis()
    }
}
