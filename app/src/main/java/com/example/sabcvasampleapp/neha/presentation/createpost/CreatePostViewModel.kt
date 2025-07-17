package com.example.sabcvasampleapp.neha.presentation.createpost

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePostViewModel(application: Application) : AndroidViewModel(application) {
    private val contentResolver: ContentResolver = application.contentResolver

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDesc: String) {
        _description.value = newDesc
    }

    fun onFileSelected(uri: Uri) {
        _selectedFileUri.value = uri
        viewModelScope.launch {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}