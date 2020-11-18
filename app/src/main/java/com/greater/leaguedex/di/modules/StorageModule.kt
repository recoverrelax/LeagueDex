package com.greater.leaguedex.di.modules

import android.content.Context
import com.greater.leaguedex.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class StorageModule {
    @Provides
    @Singleton
    fun driver(@ApplicationContext context: Context): AndroidSqliteDriver {
        return AndroidSqliteDriver(Database.Schema, context, "leaguedex.db")
    }

    @Provides
    @Singleton
    fun database(driver: AndroidSqliteDriver): Database {
        return Database.invoke(driver)
    }
}