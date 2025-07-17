package com.example.sabcvasampleapp.presentation.calendar

import androidx.lifecycle.ViewModel
import com.example.sabcvasampleapp.resources.Repository
import com.example.sabcvasampleapp.resources.EventDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CalendarViewModel : ViewModel() {
    private val _events = MutableStateFlow(Repository.getAllEvents())
    val events: StateFlow<List<EventDetails>> = _events
    private val _event = MutableStateFlow<EventDetails?>(null)
    val event: StateFlow<EventDetails?> = _event

    fun loadEvent(id: String) {
        _event.value = Repository.getEventById(id)
    }

    fun refreshEvents() {
        _events.value = Repository.getAllEvents().toList()
    }

    private val _selectedFilter = MutableStateFlow("This Week")
    val selectedFilter: StateFlow<String> = _selectedFilter

    fun updateFilter(newFilter: String) {
        _selectedFilter.value = newFilter
    }
}
