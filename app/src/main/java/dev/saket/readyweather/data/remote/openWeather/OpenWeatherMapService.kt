package dev.saket.readyweather.data.remote.openWeather

import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.openWeather.entity.OpenApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("/data/2.5/onecall?")
    suspend fun getWeatherReport(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("units") unit: String = "metric",
        @Query("appid") appID: String = Constants.OPENWEATHER_API_ACCESS_KEY
    ): OpenApiResult

}