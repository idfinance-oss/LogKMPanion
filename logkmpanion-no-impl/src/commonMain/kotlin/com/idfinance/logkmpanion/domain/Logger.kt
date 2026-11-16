package com.idfinance.logkmpanion.domain

fun addToLogKMPanion(type: LogType, tag: String, message: String) {
}

enum class LogType {
    DEFAULT,
    ERROR,
}
