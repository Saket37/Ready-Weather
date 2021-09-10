package dev.saket.readyweather.data.remote.openWeather.entity

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "timezone") val timezone: String,
    @Json(name = "timezone_offset") val timezone_offset: Int,
    @Json(name = "current") val current: Current,
    @Json(name = "daily") val daily: List<Daily>
)