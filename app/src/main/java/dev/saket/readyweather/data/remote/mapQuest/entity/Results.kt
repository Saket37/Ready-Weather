package dev.saket.readyweather.data.remote.mapQuest.entity

import com.squareup.moshi.Json

data class Results(

    @field:Json(name = "locations") val locations: List<Locations>
)