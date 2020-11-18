package com.greater.leaguedex.ui

import android.app.Application
import com.greater.leaguedex.usecase.FetchUpdateChampionUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LeagueDexApplication : Application() {
    @Inject
    lateinit var fetchChampUseCase: FetchUpdateChampionUseCase

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        GlobalScope.launch(Dispatchers.IO) {
            fetchChampUseCase.run()
        }
    }
}