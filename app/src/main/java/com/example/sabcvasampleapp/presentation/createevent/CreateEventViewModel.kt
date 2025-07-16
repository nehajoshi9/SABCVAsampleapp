package com.example.sabcvasampleapp.presentation.createevent

// CreateEventViewModel.kt

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateEventViewModel : ViewModel() {
    var title = MutableStateFlow("")
    var description = MutableStateFlow("")
    var date = MutableStateFlow("")
    var startTime = MutableStateFlow("")
    var endTime = MutableStateFlow("")
    var location = MutableStateFlow("")
    var cohosts = MutableStateFlow(listOf<String>())
    var sponsors = MutableStateFlow(listOf<String>())
    var fileUri = MutableStateFlow<Uri?>(null)
    var showDialog = MutableStateFlow(false)

    fun updateTitle(value: String) { title.value = value }
    fun updateDescription(value: String) { description.value = value }
    fun updateDate(value: String) { date.value = value }
    fun updateStartTime(value: String) { startTime.value = value }
    fun updateEndTime(value: String) { endTime.value = value }
    fun updateLocation(value: String) { location.value = value }
    fun addCohost(name: String) { cohosts.value = cohosts.value + name }
    fun removeCohost(name: String) { cohosts.value = cohosts.value - name }
    fun addSponsor(name: String) { sponsors.value = sponsors.value + name }
    fun removeSponsor(name: String) { sponsors.value = sponsors.value - name }
    fun setFile(uri: Uri?) { fileUri.value = uri }

    fun isValid(): Boolean {
        return title.value.isNotBlank() &&
                description.value.isNotBlank() &&
                date.value.isNotBlank() &&
                startTime.value.isNotBlank() &&
                endTime.value.isNotBlank() &&
                location.value.isNotBlank() &&
                cohosts.value.isNotEmpty()
    }

    fun triggerValidationDialog() {
        showDialog.value = !isValid()
    }

    fun resetValidationDialog() {
        showDialog.value = false
    }
}