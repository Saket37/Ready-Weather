package dev.saket.readyweather.data

import dev.saket.readyweather.data.remote.Location
import dev.saket.readyweather.data.remote.mapQuest.MapQuestService
import dev.saket.readyweather.data.remote.openWeather.OpenWeatherMapService
import javax.inject.Inject

class Repository @Inject constructor(
    private val openWeatherMapService: OpenWeatherMapService,
    private val mapQuestService: MapQuestService
) {
    suspend fun getWeatherReport(lat: Double, lon: Double) =
        openWeatherMapService.getWeatherReport(lat, lon)

    suspend fun getCityName(location: Location) = mapQuestService.getLocation(location = location)
}