package com.example.sabcvasampleapp.samara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import com.example.sabcva_app.screens.announcement.CreateAnnouncementScreen
import com.example.sabcva_app.screens.login.LoginScreen
import com.example.sabcva_app.screens.login.VerifyOtpScreen
import com.example.sabcva_app.screens.search.SearchResultsScreen
import com.example.sabcva_app.screens.search.viewmodel.SearchViewModel
// ... all your imports
class MainActivity : ComponentActivity() {
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var currentScreen by remember { mutableStateOf("login") }
                var phoneNumberForOtp by remember { mutableStateOf("") }
                var otpError by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            "login" -> LoginScreen(
                                onSendOtpClick = { phone ->
                                    phoneNumberForOtp = phone
                                    otpError = false
                                    currentScreen = "verifyOtp"
                                }
                            )

                            "verifyOtp" -> VerifyOtpScreen(
                                phoneNumber = phoneNumberForOtp,
                                onBackClick = { currentScreen = "login" },
                                onVerifyOtpClick = { otpCode ->
                                    otpError = false
                                    currentScreen = "search"
                                },
                                onResendOtpClick = {
                                    otpError = false
                                }
                            )

                            "search" -> SearchResultsScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = searchViewModel,
                                onHomeClick = { currentScreen = "announcement" } // ✅ ADDED
                            )

                            "announcement" -> CreateAnnouncementScreen(
                                onCancel = { currentScreen = "search" },
                                onPublish = { title, desc, uri ->
                                    println("Published: $title - $desc")
                                    println("Selected file: $uri")
                                    currentScreen = "search"
                                }
                            )

                            else -> Text("Unknown screen: $currentScreen")
                        }
                    }
                }
            }
        }
    }
}
