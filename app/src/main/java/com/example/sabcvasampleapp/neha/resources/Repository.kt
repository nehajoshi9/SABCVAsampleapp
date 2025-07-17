package com.example.sabcvasampleapp.neha.resources

import android.net.Uri
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

object Repository {
    fun getFormattedDate(date: Date?): String {
        return date?.let {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            sdf.format(it)
        } ?: ""
    }

    fun getFormattedTime(date: Date?): String {
        return date?.let {
            val formatter = SimpleDateFormat("h:mm a", Locale.getDefault()) // 12-hour format with AM/PM
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
        "This Week" to Repository::getThisWeekRange,
        "Next Week" to Repository::getNextWeekRange,
        "This Month" to Repository::getThisMonthRange
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
        val formatter = SimpleDateFormat("MMMM d", Locale.getDefault()) // e.g. July 13
        return "${formatter.format(dates.first)} – ${formatter.format(dates.second)}"
    }

    private val allEvents = mutableListOf<EventDetails>(
        EventDetails(
            id = UUID.randomUUID().toString(),
            title = "Design Workshop",
            startDate = buildDate(2025, Calendar.JULY, 17, 14, 0),
            endDate = buildDate(2025, Calendar.JULY, 17, 16, 0),
            location = "Rust Library",
            description = "Learn the latest UI/UX design trends and techniques in this hands-on workshop.",
            attendees = listOf("JD", "AK", "MR", "SL", "TW", "PJ", "TY", "QR", "BM", "SS", "SJ"),
            comments = listOf(
                Comment("John Doe", "Looking forward to this event!", 1751992800000),
                Comment("Alice Kim", "Can't wait to join.", 1751820000000)
            )
        ),
        EventDetails(
            id = UUID.randomUUID().toString(),
            title = "Tech Conference 2023",
            startDate = buildDate(2025, Calendar.JULY, 22, 9, 0),
            endDate = buildDate(2025, Calendar.JULY, 18, 17, 0),
            location = "Downtown Convention Center",
            description = "Biggest tech event of the year with industry experts.",
            attendees = listOf("SP", "LK", "AB"),
            comments = listOf(
                Comment("Raj Patel", "Super hyped!", 1751873800000)
            )
        )
    )

    private val allPersonProfiles = mutableListOf<String>(
        "Alice Johnson", "Bob Smith", "Carmen Reyes", "David Chen",
        "Eva Patel"
    )

    val allBusinessProfiles = mutableListOf<String>("Google", "Facebook", "Amazon", "Microsoft")

    val allAddresses = listOf(
        "123 Main St, New York, NY",
        "456 Maple Ave, Los Angeles, CA",
        "789 Oak Blvd, Chicago, IL",
        "1600 Pennsylvania Ave NW, Washington, DC",
        "1 Infinite Loop, Cupertino, CA"
    )
    val allProfiles = allPersonProfiles + allBusinessProfiles

    fun getAllEvents(): List<EventDetails> = allEvents.toList()

    fun getEventById(id: String): EventDetails? =
        allEvents.find { it.id == id }

    fun addAttendee(eventId: String, attendeeName: String) {
        val index = allEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = allEvents[index]
            val updatedEvent = event.copy(
                attendees = event.attendees + attendeeName
            )
            allEvents[index] = updatedEvent
        }
    }

    fun addComment(eventId: String, message: String) {
        val index = allEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = allEvents[index]
            val updatedEvent = event.copy(
                comments = listOf(
                    Comment(
                        "You",
                        message,
                        System.currentTimeMillis()
                    )
                ) + event.comments
            )
            allEvents[index] = updatedEvent
        }
    }

    fun addEvent(title: String, startDate: Date, endDate: Date, location: String, description: String, cohosts: List<String>, image: Uri? = null) {
        allEvents.add(EventDetails(UUID.randomUUID().toString(), title, startDate, endDate, location, description, listOf(), cohosts, listOf(), image))
    }

   /* fun removeEvent(eventId: String) {
        allEvents.removeIf { it.id == eventId }
    }*/
}