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
    val hosts: List<Profile> = listOf(),
    val comments: List<Comment> = listOf(),
    val fileUri: Uri? = null,
    val sponsors: List<Profile> = listOf(),
    val tags: List<String> = listOf(),
    val hasImageFile: Boolean = false
)

data class Comment(
    val name: String,
    val message: String,
    val timestamp: Long
)

data class Profile(
    val id: String,
    val name: String,
    val email: String = "",
    val profilePicture: Uri? = null,
    val type: String
)

fun createBusinessProfile(
    id: String,
    name: String,
    email: String = "",
    profilePicture: Uri? = null
): Profile {
    return Profile(id, name, email, profilePicture, type = "Business")
}

fun createPersonProfile(
    id: String,
    name: String,
    email: String = "",
    profilePicture: Uri? = null
): Profile {
    return Profile(id, name, email, profilePicture, type = "Person")
}

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
