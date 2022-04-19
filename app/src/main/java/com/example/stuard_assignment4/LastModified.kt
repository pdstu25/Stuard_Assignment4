package com.example.stuard_assignment4

import java.text.SimpleDateFormat
import java.util.*

class LastModified {
    fun getCurrentTime(): String {
        val now = Date()
        val databaseDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        var dateString : String = databaseDateFormat.format(now)

        return dateString
    }

    override fun toString(): String {
        return "${getCurrentTime()}"
    }
}