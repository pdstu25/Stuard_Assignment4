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

    fun getUTCDate(dateString : String) : String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        parser.setTimeZone(TimeZone.getTimeZone("UTC"))

        val dateInDatabase : Date = parser.parse(dateString)

        val displayFormat = SimpleDateFormat("HH:mm a MM/yyyy ")

        val displayString : String = displayFormat.format(dateInDatabase)

        return displayString
    }

    override fun toString(): String {
        return "${getCurrentTime()}"
    }
}