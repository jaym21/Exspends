package dev.jaym21.exspends.utils

import java.text.SimpleDateFormat
import java.util.*

class DateConverterUtils {

    companion object {

        fun getTimestamp(date: String): Long {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val parsedDate = sdf.parse(date)
            return parsedDate.time
        }

        fun convertDateFormat(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH)
            return sdf.format(timestamp)
        }

        fun getCurrentMonthFullName(): String {
            val sdf = SimpleDateFormat("MMMM", Locale.ENGLISH)
            return sdf.format(System.currentTimeMillis())
        }

        fun getCurrentYearShort(): String {
            val sdf = SimpleDateFormat("yy", Locale.ENGLISH)
            return sdf.format(System.currentTimeMillis())
        }
    }
}