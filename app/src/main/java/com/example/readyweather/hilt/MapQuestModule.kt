package com.example.readyweather.hilt

import com.example.readyweather.BuildConfig
import com.example.readyweather.data.remote.Constants
import com.example.readyweather.data.remote.mapQuest.MapQuestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object MapQuestModule {
    @Provides
    fun getMapQuestService(client: OkHttpClient): MapQuestService {
        return Retrofit.Builder()
            .baseUrl(Constants.MAPQUEST_API_BASE)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(MapQuestService::class.java)
    }

    /*@Provides
    fun clientMapQuestBuilder(): OkHttpClient {
        // Interceptor to enable logging in Debug
        val interceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }
        return httpClient.build()
    }*/

}