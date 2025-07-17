package com.example.sabcvasampleapp.presentation.eventpage

import androidx.lifecycle.ViewModel
import com.example.sabcvasampleapp.resources.Repository
import com.example.sabcvasampleapp.resources.EventDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventViewModel : ViewModel() {
    private val _event = MutableStateFlow<EventDetails?>(null)
    val event: StateFlow<EventDetails?> = _event

    fun loadEvent(id: String) {
        _event.value = Repository.getEventById(id)
    }
}
