// --- MainActivity.kt ---
package com.example.sabcvasampleapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sabcvasampleapp.presentation.calendar.CalendarScreen
import com.example.sabcvasampleapp.presentation.eventdetails.EventScreen
import com.example.sabcvasampleapp.presentation.createevent.CreateEventScreen
import com.example.sabcvasampleapp.ui.theme.SABCVASampleAppTheme
import com.example.sabcvasampleapp.presentation.createpost.CreatePostScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SABCVASampleAppTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "create_post") {
                        composable("calendar") {
                            CalendarScreen(navController)
                        }
                        composable("event/{eventId}") { backStackEntry ->
                            val eventId = requireNotNull(backStackEntry.arguments?.getString("eventId")) {
                                "Missing eventId in navigation route"
                            }
                            EventScreen(navController, eventId)
                        }
                        composable("create_event") {
                            CreateEventScreen(navController)
                        }
                        composable("create_post") {
                            CreatePostScreen(navController)
                        }
                    }

                }
            }
        }
    }
}