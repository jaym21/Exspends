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

        fun getCurrentYear(): String {
            val sdf = SimpleDateFormat("yyyy", Locale.ENGLISH)
            return sdf.format(System.currentTimeMillis())
        }

        fun getCurrentYearShort(): String {
            val sdf = SimpleDateFormat("yy", Locale.ENGLISH)
            return sdf.format(System.currentTimeMillis())
        }

        fun getFirstDayOfMonthTimestamp(): Long {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.clear(Calendar.MINUTE)
            cal.clear(Calendar.SECOND)
            cal.clear(Calendar.MILLISECOND)

            cal.set(Calendar.DAY_OF_MONTH, 1)

            return cal.timeInMillis
        }

        fun getFirstDayOfNextMonthTimestamp(): Long {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.clear(Calendar.MINUTE)
            cal.clear(Calendar.SECOND)
            cal.clear(Calendar.MILLISECOND)
            cal.add(Calendar.MONTH, 1)
            cal[Calendar.DATE] = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
            return cal.time.time
        }
    }
}