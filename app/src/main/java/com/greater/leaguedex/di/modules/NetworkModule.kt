package com.greater.leaguedex.di.modules

import com.greater.leaguedex.network.PrivateApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun okttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().apply {
            this.readTimeout(20, TimeUnit.SECONDS)
            this.writeTimeout(20, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl("https://ddragon.leagueoflegends.com") // we replace this later
            .build()
    }

    @Provides
    @Singleton
    fun privateApiService(
        retrofit: Retrofit,
        okttpClient: OkHttpClient
    ): PrivateApiService {
        val client = okttpClient.newBuilder().apply {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }.build()

        val retrofitBuilder = retrofit.newBuilder()
            .client(client)
            .build()

        return retrofitBuilder.create(PrivateApiService::class.java)
    }
}