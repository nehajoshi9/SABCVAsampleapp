package com.example.sabcvasampleapp

import kotlinx.coroutines.delay

class CreateAnnouncementRepository {
    suspend fun postAnnouncement(title: String, description: String, type: String): Result<Unit> {
        delay(1000) // Simulate API or DB call

        return if (title.isNotBlank()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Title cannot be empty"))
        }
    }
}