package dev.saket.readyweather.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateHelper {

    fun Long.unixSecondsToText(format: Format, upperCase: Boolean = false): String {
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(this),
            TimeZone.getTimeZone("Asia/Kolkata").toZoneId()
        )

        return if (upperCase) {
            date.format(DateTimeFormatter.ofPattern(format)).uppercase(Locale.getDefault())
        } else {
            date.format(DateTimeFormatter.ofPattern(format))
        }
    }
}