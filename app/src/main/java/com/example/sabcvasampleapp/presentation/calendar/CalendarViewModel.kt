package com.example.sabcvasampleapp.presentation.calendar

import androidx.lifecycle.ViewModel
import com.example.sabcvasampleapp.presentation.data.EventRepository
import com.example.sabcvasampleapp.presentation.data.EventDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CalendarViewModel : ViewModel() {
    private val _events = MutableStateFlow(EventRepository.getAllEvents())
    val events: StateFlow<List<EventDetails>> = _events
    private val _event = MutableStateFlow<EventDetails?>(null)
    val event: StateFlow<EventDetails?> = _event

    fun loadEvent(id: String) {
        _event.value = EventRepository.getEventById(id)
    }

    fun refreshEvents() {
        _events.value = EventRepository.getAllEvents().toList()
    }
}
