package com.idfinance.logkmpanion.presentation.extensions

import io.ktor.util.date.GMTDate

fun GMTDate.toUIFormat(): String {
    val formattedHours = if (hours < 10) "0${hours}" else hours.toString()
    val formattedMinutes = if (minutes < 10) "0${minutes}" else minutes.toString()
    val formattedSeconds = if (seconds < 10) "0${seconds}" else seconds.toString()
    return "$formattedHours:$formattedMinutes:$formattedSeconds"
}