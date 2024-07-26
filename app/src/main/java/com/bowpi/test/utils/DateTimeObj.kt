package com.bowpi.test.utils


data class DateTimeObj(val date: String, val time: String) {
    companion object {
        fun fromTimestamp(timestamp: Long) =
            DateTimeObj(
                DateFormat.format(timestamp, "dd/MM/yyyy"),
                DateFormat.format(timestamp, "HH:mm:ss.SSS")
            )
    }
}

