package dev.saket.readyweather.hilt

import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.openWeather.OpenWeatherMapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object OpenWeatherMapModule {
    @Provides
    fun getService(client: OkHttpClient): OpenWeatherMapService {
        return Retrofit.Builder()
            .baseUrl(Constants.OPENWEATHER_API_BASE)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(OpenWeatherMapService::class.java)
    }

    @Provides
    fun clientBuilder(): OkHttpClient {
        // Interceptor to enable logging in Debug
        val interceptor = HttpLoggingInterceptor().apply {
            level =
                if (dev.saket.readyweather.BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            connectTimeout(2, TimeUnit.MINUTES)
            readTimeout(2, TimeUnit.MINUTES)
            writeTimeout(4, TimeUnit.MINUTES)
        }

        return httpClient.build()
    }
}