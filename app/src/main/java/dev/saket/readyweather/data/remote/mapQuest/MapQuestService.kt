package dev.saket.readyweather.data.remote.mapQuest

import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.Location
import dev.saket.readyweather.data.remote.mapQuest.entity.MapQuestResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MapQuestService {
    @GET("geocoding/v1/reverse")
    suspend fun getLocation(
        @Query("key") key: String = Constants.MAPQUEST_API_ACCESS_KEY,
        @Query("location", encoded = true) location: Location
    ): MapQuestResult

}
