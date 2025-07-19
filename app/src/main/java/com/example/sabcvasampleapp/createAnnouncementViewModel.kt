package com.example.sabcvasampleapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


// ViewModel
class CreateAnnouncementViewModel(
    private val repository: CreateAnnouncementRepository = CreateAnnouncementRepository()
) : ViewModel() {

    // State holding object
    var uiState by mutableStateOf(CreateAnnouncementUiState())
        private set

    fun selectType(type: String) {
        uiState = uiState.copy(selectedType = type)
    }

    fun updateTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun updateDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun postAnnouncement() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, isPosted = false)

            val result = repository.postAnnouncement(
                title = uiState.title,
                description = uiState.description,
                type = uiState.selectedType
            )

            uiState = if (result.isSuccess) {
                uiState.copy(isLoading = false, isPosted = true)
            } else {
                uiState.copy(
                    isLoading = false,
                    isPosted = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    data class CreateAnnouncementUiState(
        val selectedType: String = "Announcement",
        val title: String = "",
        val description: String = "",
        val isLoading: Boolean = false,
        val isPosted: Boolean = false,
        val errorMessage: String? = null
    )
}
