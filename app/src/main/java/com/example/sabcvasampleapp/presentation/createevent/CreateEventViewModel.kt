package com.example.sabcvasampleapp.presentation.createevent

// CreateEventViewModel.kt

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date

class CreateEventViewModel : ViewModel() {
    var title = MutableStateFlow("")
    var description = MutableStateFlow("")
    //var date = MutableStateFlow("")
    var location = MutableStateFlow("")
    var cohosts = MutableStateFlow(listOf<String>())
    var sponsors = MutableStateFlow(listOf<String>())
    var fileUri = MutableStateFlow<Uri?>(null)
    var showDialog = MutableStateFlow(false)

    private val _locationValid = MutableStateFlow(false)
    val isLocationValid: StateFlow<Boolean> = _locationValid

    fun updateTitle(value: String) { title.value = value }
    fun updateDescription(value: String) { description.value = value }

    private val _eventDate = MutableStateFlow<Date?>(null)
    val eventDate: StateFlow<Date?> = _eventDate

    fun updateDateFromPicker(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
            set(Calendar.HOUR_OF_DAY, 0) // optional: normalize time
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        _eventDate.value = calendar.time
    }

    private val _startTime = MutableStateFlow<Date?>(null)
    val startTime: StateFlow<Date?> = _startTime

    private val _endTime = MutableStateFlow<Date?>(null)
    val endTime: StateFlow<Date?> = _endTime

    fun updateStartTime(hour: Int, minute: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        _startTime.value = cal.time
    }

    fun updateEndTime(hour: Int, minute: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        _endTime.value = cal.time
    }

    fun updateLocation(value: String) { location.value = value }
    fun addCohost(name: String) { cohosts.value = cohosts.value + name }
    fun removeCohost(name: String) { cohosts.value = cohosts.value - name }
    fun addSponsor(name: String) { sponsors.value = sponsors.value + name }
    fun removeSponsor(name: String) { sponsors.value = sponsors.value - name }
    fun setFile(uri: Uri?) { fileUri.value = uri }

    fun isValid(): Boolean {
        /*
        val locationOk = isLocationValid.value
        val titleOk = title.value.isNotBlank()
        val descriptionOk = description.value.isNotBlank()
        val dateOk = eventDate.value != null
        val startTimeOk = startTime.value != null
        val endTimeOk = endTime.value != null
        val locationTextOk = location.value.isNotBlank()
        val cohostsOk = cohosts.value.isNotEmpty()

        println("isLocationValid: $locationOk")
        println("title not blank: $titleOk")
        println("description not blank: $descriptionOk")
        println("date not null: $dateOk")
        println("startTime not null: $startTimeOk")
        println("endTime not null: $endTimeOk")
        println("location text not blank: $locationTextOk")
        println("cohosts not empty: $cohostsOk") */

        return isLocationValid.value && title.value.isNotBlank() &&
                description.value.isNotBlank() &&
                eventDate.value != null &&
                startTime.value != null &&
                endTime.value != null &&
                location.value.isNotBlank() &&
                cohosts.value.isNotEmpty()
    }

    fun triggerValidationDialog() {
        showDialog.value = !isValid()
    }

    fun resetValidationDialog() {
        showDialog.value = false
    }


    fun updateIsLocationValid(valid: Boolean) {
        _locationValid.value = valid
    }
}