package com.example.sabcvasampleapp.neha.presentation.createevent

// CreateEventViewModel.kt

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.sabcvasampleapp.neha.resources.Profile
import com.example.sabcvasampleapp.neha.resources.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date

class CreateEventViewModel : ViewModel() {
    var title = MutableStateFlow("")
    var description = MutableStateFlow("")
    var location = MutableStateFlow("")
    var hosts = MutableStateFlow(listOf<Profile>())
    var sponsors = MutableStateFlow(listOf<Profile>())
    var fileUri = MutableStateFlow<Uri?>(null)
    var showDialog = MutableStateFlow(false)
    var errors = MutableStateFlow(listOf<String>())

    var tags = MutableStateFlow(listOf<String>())

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
    fun addHost(host: Profile) { hosts.value = hosts.value + host }
    fun removeHost(host: Profile) { hosts.value = hosts.value - host }
    fun addSponsor(sponsor: Profile) { sponsors.value = sponsors.value + sponsor }
    fun removeSponsor(sponsor: Profile) { sponsors.value = sponsors.value - sponsor }
    fun setFile(uri: Uri?) { fileUri.value = uri }
    fun addTag(tag: String) {
        Repository.tagUsageMap[tag] = Repository.tagUsageMap.getOrDefault(tag, 0) + 1
        tags.value = tags.value + tag
    }
    fun removeTag(tag: String) {
        Repository.tagUsageMap[tag] = Repository.tagUsageMap.getOrDefault(tag, 1) - 1
        if (Repository.tagUsageMap[tag] == 0) {
            Repository.tagUsageMap.remove(tag)
        }
        tags.value = tags.value - tag
    }
    fun isValid(): Boolean {
        val errorList = mutableListOf<String>()

        if (title.value.isBlank()) errorList.add("Title is required.")
        if (description.value.isBlank()) errorList.add("Description is required.")
        if (eventDate.value == null) errorList.add("Date is required.")
        if (startTime.value == null) errorList.add("Start time is required.")
        if (endTime.value == null) errorList.add("End time is required.")
        if (location.value.isBlank()) {
            errorList.add("Location is required.")
        } else if (!isLocationValid.value) {
            errorList.add("Selected location is invalid.")
        }
        if (hosts.value.isEmpty()) errorList.add("At least one host is required.")

        errors.value = errorList
        return errorList.isEmpty()
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