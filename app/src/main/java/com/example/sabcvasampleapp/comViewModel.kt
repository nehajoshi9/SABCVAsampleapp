package com.example.sabcvasampleapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class comViewModel : ViewModel() {

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    var selectedFilter by mutableStateOf("All")
        private set

    init {
        loadPosts()
    }

    private fun loadPosts() {
        fireBaseRepo.getPosts { fetchedPosts ->
            posts = fetchedPosts
        }
    }

    fun addPost(post: Post) {
        fireBaseRepo.addPost(post)
    }

    fun updateFilter(filter: String) {
        selectedFilter = filter
    }

    fun getFilteredPosts(): List<Post> {
        return if (selectedFilter == "All") posts
        else posts.filter { it.type == selectedFilter }
    }
}
