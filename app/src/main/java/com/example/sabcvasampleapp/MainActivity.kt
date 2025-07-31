// --- MainActivity.kt ---
package com.example.sabcvasampleapp

import android.R.style.Theme
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sabcvasampleapp.presentation.calendar.CalendarScreen
import com.example.sabcvasampleapp.presentation.eventpage.EventScreen
import com.example.sabcvasampleapp.presentation.createevent.CreateEventScreen
import com.example.sabcvasampleapp.ui.theme.SABCVASampleAppTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: comViewModel = viewModel()

            NavHost(navController, startDestination = "community") {
                composable("community") {
                    CommunityScreen(
                        posts = viewModel.posts,
                        onNavigateToCreatePost = {
                            navController.navigate("createPost")
                        }
                    )
                }

                composable("createPost") {
                    CreatePostScreen(
                        onPost = { newPost ->
                            viewModel.addPost(newPost)
                            navController.popBackStack()
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CommunityScreen(
    posts: List<Post>,
    onNavigateToCreatePost: () -> Unit
) {
    val selectedFilter = remember { mutableStateOf("All") }

    val filteredPosts = remember(selectedFilter.value, posts) {
        if (selectedFilter.value == "All") {
            posts
        } else {
            posts.filter { it.tag.equals(selectedFilter.value, ignoreCase = true) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F7))
                .padding(bottom = 80.dp)
        ) {
            TopBar(onPostClick = onNavigateToCreatePost)
            FilterChips(selected = selectedFilter.value) { selectedFilter.value = it }
            CreatePostSection(onClick = onNavigateToCreatePost)

            filteredPosts.forEach { post ->
                PostCard(post = post)
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavigationBar()
        }
    }
}

@Composable
fun TopBar(onPostClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text("SABC", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Community", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onPostClick) {
            Icon(Icons.Default.Edit, contentDescription = "Post")
        }
    }
}

@Composable
fun FilterChips(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("All", "Announcement", "Event", "Tweet")
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        options.forEach {
            FilterChip(
                selected = selected == it,
                onClick = { onSelect(it) },
                label = { Text(it) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
@Composable
fun CreatePostSection(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleAvatar("SA")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Create an announcement or post...", color = Color.Gray)
    }
}

@Composable
fun CircleAvatar(initials: String) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") }, selected = true, onClick = {})
        NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, label = { Text("Search") }, selected = false, onClick = {})
        NavigationBarItem(icon = {
            BadgedBox(badge = { Badge { Text("3") } }) {
                Icon(Icons.Default.Email, contentDescription = null)
            }
        }, label = { Text("Messages") }, selected = false, onClick = {})
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = {})
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${post.name} • ${post.role} @ ${post.business}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(post.title, style = MaterialTheme.typography.titleMedium)

            // Tag badge
            Box(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp)
                    .background(
                        color = when (post.tag) {
                            "Announcement" -> Color(0xFF1976D2) // Blue
                            "Event" -> Color(0xFF388E3C)        // Green
                            "Tweet" -> Color(0xFFD32F2F)        // Red
                            else -> Color.Gray
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = post.tag,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Text(post.content, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun CreatePostScreen(
    onPost: (Post) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var business by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("") }

    val tagOptions = listOf("Announcement", "Event", "Tweet")

    val isFormValid = name.isNotBlank() &&
            role.isNotBlank() &&
            business.isNotBlank() &&
            title.isNotBlank() &&
            content.isNotBlank() &&
            selectedTag.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Create New Post", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Your Role") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = business,
            onValueChange = { business = it },
            label = { Text("Your Business") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Post Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("What would you like to share?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Post Type", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            tagOptions.forEach { tag ->
                FilterChip(
                    selected = selectedTag == tag,
                    onClick = { selectedTag = tag },
                    label = { Text(tag) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = when (tag) {
                        "Announcement" -> FilterChipDefaults.filterChipColors(containerColor = Color(0xFFBBDEFB))
                        "Event" -> FilterChipDefaults.filterChipColors(containerColor = Color(0xFFC8E6C9))
                        "Tweet" -> FilterChipDefaults.filterChipColors(containerColor = Color(0xFFFFCDD2))
                        else -> FilterChipDefaults.filterChipColors()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (isFormValid) {
                        onPost(
                            Post(
                                name = name,
                                role = role,
                                business = business,
                                title = title,
                                content = content,
                                tag = selectedTag
                            )
                        )
                    } else {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isFormValid
            ) {
                Text("Post")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}






