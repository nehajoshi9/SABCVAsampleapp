package com.example.sabcvasampleapp.presentation.data

import com.example.sabcvasampleapp.presentation.data.Comment
import com.example.sabcvasampleapp.presentation.data.EventDetails

object EventRepository {
    private val allEvents = mutableListOf<EventDetails>(
        EventDetails(
            id = "design",
            title = "Design Workshop",
            date = "Thu, Jun 14 • 2:00 PM – 4:00 PM",
            location = "Creative Hub, 123 Design St",
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
            date = "Wed, Jun 15 • 9:00 AM – 5:00 PM",
            location = "Downtown Convention Center",
            description = "Biggest tech event of the year with industry experts.",
            attendees = listOf("SP", "LK", "AB"),
            comments = listOf(
                Comment("Raj Patel", "Super hyped!", 1751873800000)
            )
        )
    )

    private val allPersonProfiles = mutableListOf<String>("Eva Patel", "")



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

    fun addEvent(id: String, title: String, date: String, location: String, description: String) =
        allEvents.add(EventDetails(id, title, date, location, description, listOf(), listOf()))

    fun removeEvent(eventId: String) {
        allEvents.removeIf { it.id == eventId }
    }
}