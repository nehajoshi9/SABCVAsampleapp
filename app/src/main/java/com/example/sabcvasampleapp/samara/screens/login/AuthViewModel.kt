package com.example.sabcva_app.screens.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun signIn(phone: String, password: String) {
        // dummy check
        _isAuthenticated.value = phone == "555" && password == "1234"
    }

    fun sendResetCode(phone: String) {
        println("Sending reset code to: $phone")
    }
}
