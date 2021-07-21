package com.example.readyweather.data.remote.openWeather.entity

data class OpenApiResult(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
)