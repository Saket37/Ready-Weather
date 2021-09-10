package dev.saket.readyweather.data.remote.openWeather.entity

import com.squareup.moshi.Json

data class Temp(

    @Json(name = "day") val day: Double,
    @Json(name = "min") val min: Double,
    @Json(name = "max") val max: Double,
    @Json(name = "night") val night: Double,
    @Json(name = "eve") val eve: Double,
    @Json(name = "morn") val morn: Double
)