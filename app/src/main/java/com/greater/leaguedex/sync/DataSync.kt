package com.greater.leaguedex.sync

import com.greater.leaguedex.storage.store.SettingStore
import com.greater.leaguedex.usecase.FetchAndUpdatePeople
import com.greater.leaguedex.usecase.FetchAndUpdateSpecies
import com.greater.leaguedex.usecase.FetchAndUpdateVehicles
import com.greater.leaguedex.util.UpdateStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataSync @Inject constructor(
    private val fetchAndUpdatePeople: FetchAndUpdatePeople,
    private val fetchAndUpdateSpecie: FetchAndUpdateSpecies,
    private val fetchAndUpdateVehicles: FetchAndUpdateVehicles,
    private val settingStore: SettingStore
) {
    private val REFRESH_TIMEOUT = TimeUnit.HOURS.toMillis(1)

    suspend fun isSyncNeeded(): Boolean {
        return settingStore.getLastRefresh().shouldSync()
    }

    fun sync(syncNeeded: Boolean): Flow<UpdateStatus> {
        return flow {
            if (!syncNeeded) {
                emit(UpdateStatus.FINISHED)
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
        }.onEach { Timber.i("State: $it") }
    }

    private suspend fun updateSyncStatusSuccess() {
        settingStore.insert(
            lastRefresh = System.currentTimeMillis()
        )
    }

    private fun Long?.shouldSync(): Boolean {
        if (this == null) return true
        return this + REFRESH_TIMEOUT <= System.currentTimeMillis()
    }
}
