// 1. EventDetails.kt
package com.example.sabcvasampleapp.presentation.data

data class EventDetails(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val description: String,
    val attendees: List<String>,
    val comments: List<Comment>,
    //val rsvpButton: Boolean
)

data class Comment(
    val name: String,
    val message: String,
    val timestamp: Long
)
