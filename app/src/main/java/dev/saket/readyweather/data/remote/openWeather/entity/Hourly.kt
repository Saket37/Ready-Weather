package dev.saket.readyweather.data.remote.openWeather.entity

import com.squareup.moshi.Json

data class Hourly(
    @field:Json(name = "dt") val date: Long,
    @field:Json(name = "temp") val temp: Double,
    @field:Json(name = "feels_like") val feels_like: Float,
    @field:Json(name = "pressure") val pressure: Int,
    @field:Json(name = "humidity") val humidity: Float,
    @field:Json(name = "dew_point") val dew_point: Double,
    @field:Json(name = "uvi") val uvi: Double,
    @field:Json(name = "clouds") val clouds: Int,
    @field:Json(name = "visibility") val visibility: Int,
    @field:Json(name = "wind_speed") val wind_speed: Double,
    @field:Json(name = "wind_deg") val wind_deg: Int,
    @field:Json(name = "wind_gust") val wind_gust: Double,
    @field:Json(name = "weather") val weather: List<Weather>,
    @field:Json(name = "pop") val pop: Double
)