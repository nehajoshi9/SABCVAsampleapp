// 1. Classes.kt
package com.example.sabcvasampleapp.neha.resources
import android.net.Uri
import java.util.Date

data class EventDetails(
    val id: String,
    val title: String,
    val startDate: Date,
    val endDate: Date,
    val location: String,
    val description: String,
    val attendees: List<String> = listOf(),
    val hosts: List<String> = listOf(),
    val comments: List<Comment> = listOf(),
    val image: Uri? = null
)

data class Comment(
    val name: String,
    val message: String,
    val timestamp: Long
)

data class UserDetails(
    val name: String,
    val title: String,
    val phone: Number,
    val id: Number
)

data class PostDetails(
    val title: String,
    val description: String,
    val id: Number
)

data class AnnouncementDetails(
    val title: String,
    val description: String,
    val id: Number
)
