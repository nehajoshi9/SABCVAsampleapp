package com.example.sabcvasampleapp.neha.presentation.calendar

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sabcvasampleapp.neha.resources.Repository
import com.example.sabcvasampleapp.neha.resources.EventDetails
import java.util.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

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

    //var selectedFilter by mutableStateOf("This Week")
    val rangeOffset = mutableStateOf(0)
    fun incrementOffset() {
        rangeOffset.value++
        if (rangeOffset.value != 0) _selectedFilter.value = ""
    }

    fun decrementOffset() {
        rangeOffset.value--
        if (rangeOffset.value != 0) _selectedFilter.value = ""
    }

    fun getCurrentDateRange(): Pair<Date, Date> {
        val baseRange = Repository.dateRangeMap[selectedFilter.value]?.invoke() ?: Repository.getThisWeekRange()
        return shiftDateRange(baseRange, selectedFilter.value, rangeOffset.value)
    }

    fun shiftDateRange(base: Pair<Date, Date>, type: String, offset: Int): Pair<Date, Date> {
        val calStart = Calendar.getInstance().apply { time = base.first }
        val calEnd = Calendar.getInstance().apply { time = base.second }

        val field = when (type) {
            "This Week", "Next Week" -> Calendar.WEEK_OF_YEAR
            "This Month" -> Calendar.MONTH
            else -> Calendar.WEEK_OF_YEAR
        }

        calStart.add(field, offset)
        calEnd.add(field, offset)

        return Pair(calStart.time, calEnd.time)
    }


    fun updateFilter(newFilter: String) {
        _selectedFilter.value = newFilter
        rangeOffset.value = 0
    }
}
