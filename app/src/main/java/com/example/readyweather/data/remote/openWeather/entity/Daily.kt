package com.example.readyweather.data.remote.openWeather.entity

import com.squareup.moshi.Json

data class Daily(
    @field:Json(name = "dt") val date: Long,
    @field:Json(name = "sunrise") val sunrise: Long,
    @field:Json(name = "sunset") val sunset: Long,
    @field:Json(name = "moonrise") val moonrise: Long,
    @field:Json(name = "moonset") val moonset: Long,
    @field:Json(name = "moon_phase") val moon_phase: Double,
    @field:Json(name = "temp") val temp: Temp,
    @field:Json(name = "feels_like") val feels_like: FeelsLike,
    @field:Json(name = "pressure") val pressure: Int,
    @field:Json(name = "humidity") val humidity: Float,
    @field:Json(name = "dew_point") val dew_point: Double,
    @field:Json(name = "wind_speed") val wind_speed: Double,
    @field:Json(name = "wind_deg") val wind_deg: Float,
    @field:Json(name = "wind_gust") val wind_gust: Double,
    @field:Json(name = "weather") val weather: List<Weather>,
    @field:Json(name = "clouds") val clouds: Int,
    @field:Json(name = "pop") val pop: Double,
    @field:Json(name = "rain") val rain: Double,
    @field:Json(name = "uvi") val uvi: Double
)