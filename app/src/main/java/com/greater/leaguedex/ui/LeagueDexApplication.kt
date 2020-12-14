package com.greater.leaguedex.ui

import android.app.Application
import com.facebook.stetho.Stetho
import com.greater.leaguedex.usecase.FetchAndUpdatePeople
import com.greater.leaguedex.usecase.FetchAndUpdateSpecies
import com.greater.leaguedex.usecase.FetchAndUpdateVehicles
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LeagueDexApplication : Application() {
    @Inject lateinit var fetchAndUpdatePeople: FetchAndUpdatePeople
    @Inject lateinit var fetchAndUpdateSpecie: FetchAndUpdateSpecies
    @Inject lateinit var fetchAndUpdateVehicles: FetchAndUpdateVehicles

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this);

        GlobalScope.launch(Dispatchers.Main.immediate) {
//            fetchAndUpdatePeople()
//            fetchAndUpdateSpecie()
//            fetchAndUpdateVehicles()
        }
    }
}
