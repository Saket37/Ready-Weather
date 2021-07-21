package com.example.readyweather.data.remote.openWeather.entity

import com.squareup.moshi.Json

data class Weather(

    @Json(name = "id") val id: Int,
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)