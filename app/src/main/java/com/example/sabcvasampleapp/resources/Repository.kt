package com.example.sabcvasampleapp.resources

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

object Repository {
    fun buildDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        return Calendar.getInstance().apply {
            set(year, month, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }



    fun formatEventDateRange(start: Date, end: Date): String {
        val dayFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault()) // Wed, Jun 15
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())    // 9:00 AM

        val dayPart = dayFormat.format(start)
        val startTime = timeFormat.format(start)
        val endTime = timeFormat.format(end)

        return "$dayPart • $startTime – $endTime"
    }

    private val allEvents = mutableListOf<EventDetails>(
        EventDetails(
            id = "design",
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
            id = "tech",
            title = "Tech Conference 2023",
            startDate = buildDate(2025, Calendar.JULY, 18, 9, 0),
            endDate = buildDate(2025, Calendar.JULY, 18, 17, 0),
            location = "Downtown Convention Center",
            description = "Biggest tech event of the year with industry experts.",
            attendees = listOf("SP", "LK", "AB"),
            comments = listOf(
                Comment("Raj Patel", "Super hyped!", 1751873800000)
            )
        )
    )

    private val allPersonProfiles = mutableListOf<String>("Alice Johnson", "Bob Smith", "Carmen Reyes", "David Chen",
        "Eva Patel")

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

    /*fun addEvent(id: String, title: String, date: String, location: String, description: String) =
        allEvents.add(EventDetails(id, title, date, location, description, listOf(), listOf()))

    fun removeEvent(eventId: String) {
        allEvents.removeIf { it.id == eventId }
    }*/
}