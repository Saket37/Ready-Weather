package com.example.readyweather.utils

typealias Format = String

object DateFormat {
    const val DATE_WITH_DAY: Format = "EEEE, d MMMM"
    const val DATE_WITH_3_LETTER_DAY = "EEE, MMM d"
    const val TIME_12HOUR: Format = "h:mm a"
    const val ONLY_DAY_OF_MONTH: Format = "d"
}