package com.example.sabcvasampleapp.neha.resources

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utilities {
    fun getFormattedDate(date: Date?): String {
        return date?.let {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            sdf.format(it)
        } ?: ""
    }

    fun getFormattedTime(date: Date?): String {
        return date?.let {
            val formatter =
                SimpleDateFormat("h:mm a", Locale.getDefault()) // 12-hour format with AM/PM
            formatter.format(it)
        } ?: ""
    }

    fun buildDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        return Calendar.getInstance().apply {
            set(year, month, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    fun getThisWeekRange(): Pair<Date, Date> {
        val calendar = Calendar.getInstance()

        // Set to start of week (Sunday)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        val startOfWeek = calendar.time

        // Set to end of week (Saturday 11:59:59 PM)
        calendar.add(Calendar.DAY_OF_WEEK, 6)

        val endOfWeek = calendar.time

        return Pair(startOfWeek, endOfWeek)
    }

    val dateRangeMap: Map<String, () -> Pair<Date, Date>> = mapOf(
        "This Week" to Utilities::getThisWeekRange,
        "Next Week" to Utilities::getNextWeekRange,
        "This Month" to Utilities::getThisMonthRange
    )


    fun getNextWeekRange(): Pair<Date, Date> {
        val (thisWeekStart, _) = getThisWeekRange()

        val start = Calendar.getInstance().apply {
            time = thisWeekStart
            add(Calendar.DAY_OF_YEAR, 7)
        }
        val end = (start.clone() as Calendar).apply {
            add(Calendar.DAY_OF_WEEK, 6)
        }

        return Pair(start.time, end.time)
    }

    fun getThisMonthRange(): Pair<Date, Date> {
        val start = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val end = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        return Pair(start.time, end.time)
    }


    fun formatEventDateRange(start: Date, end: Date): String {
        val dayFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault()) // Wed, Jun 15

        val dayPart = dayFormat.format(start)
        val startTime = getFormattedTime(start)
        val endTime = getFormattedTime(end)

        return "$dayPart • $startTime – $endTime"
    }

    fun isDateInRangeInclusive(target: Date, range: Pair<Date, Date>): Boolean {
        val dateOnlyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val targetStr = dateOnlyFormat.format(target)
        val startStr = dateOnlyFormat.format(range.first)
        val endStr = dateOnlyFormat.format(range.second)

        return targetStr >= startStr && targetStr <= endStr
    }

    fun formatDateRangeString(dates: Pair<Date, Date>): String {
        Calendar.getInstance().apply { time = dates.second }.get(Calendar.YEAR)

        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return "${formatter.format(dates.first)} – ${formatter.format(dates.second)}"

    }
}