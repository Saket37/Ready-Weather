package com.example.readyweather.utils

import java.util.*

object CapitalizeLetterHelper {
    fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

}