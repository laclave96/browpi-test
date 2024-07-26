package com.bowpi.test.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormat {

    companion object {
        fun format(timestamp: Long, format: String): String {
            val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            return simpleDateFormat.format(Date(timestamp))
        }

    }

}