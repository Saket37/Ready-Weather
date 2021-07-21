package com.example.readyweather.data.remote.openWeather.entity

import com.squareup.moshi.Json

data class Current(
    @field:Json(name = "dt") val date: Long,
    @field:Json(name = "sunrise") val sunrise: Long,
    @field:Json(name = "sunset") val sunset: Long,
    @field:Json(name = "temp") val temp: Double,
    @field:Json(name = "feels_like") val feelsLike: Float,
    @field:Json(name = "pressure") val pressure: Int,
    @field:Json(name = "humidity") val humidity: Float,
    @field:Json(name = "dew_point") val dewPoint: Double,
    @field:Json(name = "uvi") val uvi: Double,
    @field:Json(name = "clouds") val clouds: Int,
    @field:Json(name = "visibility") val visibility: Int,
    @field:Json(name = "wind_speed") val windSpeed: Float,
    @field:Json(name = "wind_deg") val windDeg: Float,
    @field:Json(name = "weather") val weather: List<Weather>
)