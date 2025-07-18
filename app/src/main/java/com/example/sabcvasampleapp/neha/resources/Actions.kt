package com.example.sabcvasampleapp.neha.resources

import android.net.Uri
import java.util.Date
import java.util.UUID

object Actions {
    fun addAttendee(eventId: String, attendeeName: String) {
        val allEvents = Data.getAllEventsMutable()
        val index = allEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = allEvents[index]
            Data.updateEvent(index, event.copy(attendees = event.attendees + attendeeName))
        }
    }

    fun removeAttendee(eventId: String, attendeeName: String) {
        val allEvents = Data.getAllEventsMutable()
        val index = allEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = allEvents[index]
            Data.updateEvent(index, event.copy(attendees = event.attendees - attendeeName))
        }
    }

    fun addComment(eventId: String, message: String) {
        val allEvents = Data.getAllEventsMutable()
        val index = allEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val event = allEvents[index]
            val updatedEvent = event.copy(
                comments = listOf(
                    Comment("You", message, System.currentTimeMillis())
                ) + event.comments
            )
            Data.updateEvent(index, updatedEvent)
        }
    }

    fun addEvent(
        title: String,
        startDate: Date,
        endDate: Date,
        location: String,
        description: String,
        hosts: List<Profile>,
        image: Uri? = null,
        sponsors: List<Profile> = listOf(),
        tags: List<String> = listOf(),
        isImage: Boolean = false
    ) {
        val allEvents = Data.getAllEventsMutable()
        allEvents.add(
            EventDetails(
                UUID.randomUUID().toString(),
                title,
                startDate,
                endDate,
                location,
                description,
                listOf(),
                hosts,
                listOf(),
                image,
                sponsors,
                tags,
                isImage
            )
        )
    }

    fun removeEvent(eventId: String) {
        val allEvents = Data.getAllEventsMutable()
        val index = allEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            allEvents.removeAt(index)
        }
    }
}
