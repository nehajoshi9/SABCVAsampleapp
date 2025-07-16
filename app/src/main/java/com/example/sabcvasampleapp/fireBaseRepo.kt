package com.example.sabcvasampleapp

import android.app.DownloadManager

object fireBaseRepo {
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    fun getPosts(callback: (List<Post>) -> Unit) {
        postsCollection.orderBy("timestamp", DownloadManager.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val posts = snapshot.toObjects(Post::class.java)
                callback(posts)
            }
    }

    fun addPost(post: Post) {
        postsCollection.document(post.id).set(post)
    }
}
