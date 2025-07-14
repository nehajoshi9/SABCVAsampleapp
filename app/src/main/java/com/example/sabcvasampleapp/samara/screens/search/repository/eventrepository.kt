package com.example.sabcva_app.screens.search.repository

import com.example.sabcva_app.screens.search.data.Events

class eventrepository {

    private val eventList = mutableListOf(
        Events(eventId = "1", title = "SABC Annual Meetup", date = "July 15, 2025", location = "Tysons Corner, VA", description = "Annual meetup for South Asian Business Community.", tags = arrayListOf("South Asian", "Business")),
        Events(eventId = "2", title = "Women Founders Panel", date = "August 1, 2025", location = "Arlington, VA", description = "Panel discussion with women entrepreneurs.", tags = arrayListOf("Entrepreneurship", "Diversity", "Women"))
    )

    // Read
    fun getEvents(): List<Events> = eventList.toList()

    // Create
    fun addEvent(event: Events) {
        eventList.add(event)
    }

    // Update
    fun updateEvent(updatedEvent: Events) {
        val index = eventList.indexOfFirst { it.eventId == updatedEvent.eventId }
        if (index != -1) {
            eventList[index] = updatedEvent
        }
    }

    // Delete
    fun deleteEvent(eventId: String) {
        eventList.removeAll { it.eventId == eventId }
    }
}
