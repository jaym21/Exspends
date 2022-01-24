package dev.jaym21.exspends.utils

import org.junit.Assert.assertNotNull
import org.junit.Test


class DateConverterUtilsTest {

    @Test
    fun getTimestampTest() {
        val timestamp = DateConverterUtils.getTimestamp("23/01/2022")
        assertNotNull(timestamp)
    }

    @Test
    fun convertDateFormatTest() {
        val formattedDate = DateConverterUtils.convertDateFormat(1642876200000)
        assertNotNull(formattedDate)
    }

    @Test
    fun getCurrentMonthFullNameTest() {
        val month = DateConverterUtils.getCurrentMonthFullName()
        assertNotNull(month)
    }

    @Test
    fun getCurrentYear() {
        val year = DateConverterUtils.getCurrentYear()
        assertNotNull(year)
    }

    @Test
    fun getCurrentYearShortTest() {
        val year = DateConverterUtils.getCurrentYearShort()
        assertNotNull(year)
    }

    @Test
    fun getFirstDayOfMonthTimestampTest() {
        val firstDay = DateConverterUtils.getFirstDayOfMonthTimestamp()
        assertNotNull(firstDay)
    }

    @Test
    fun getFirstDayOfNextMonthTimestampTest() {
        val firstDay = DateConverterUtils.getFirstDayOfNextMonthTimestamp()
        assertNotNull(firstDay)
    }
}