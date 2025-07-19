package com.example.sabcvasampleapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class comViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        db.collection("posts").get()
            .addOnSuccessListener { result ->
                _posts.clear()
                for (document in result) {
                    val post = document.toObject(Post::class.java)
                    _posts.add(post)
                }
            }
    }
}



