package com.example.readyweather.data

import com.example.readyweather.data.remote.Location
import com.example.readyweather.data.remote.mapQuest.MapQuestService
import com.example.readyweather.data.remote.openWeather.OpenWeatherMapService
import javax.inject.Inject

class Repository @Inject constructor(
    private val openWeatherMapService: OpenWeatherMapService,
    private val mapQuestService: MapQuestService
) {
    suspend fun getWeatherReport(lat: Double, lon: Double) =
        openWeatherMapService.getWeatherReport(lat, lon)

    suspend fun getCityName(location: Location) = mapQuestService.getLocation(location = location)
}