package com.example.sabcvasampleapp

data class Post(
    val name:String= "",
    val role: String = "",
    val business: String = "",
    val title: String = "",
    val content: String = "",
    val tag: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
