package dev.saket.readyweather.data.remote

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "${String.format("%.2f", latitude)},${String.format("%.2f", longitude)}"
    }
}