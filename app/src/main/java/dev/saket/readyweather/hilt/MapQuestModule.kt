package dev.saket.readyweather.hilt

import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.mapQuest.MapQuestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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