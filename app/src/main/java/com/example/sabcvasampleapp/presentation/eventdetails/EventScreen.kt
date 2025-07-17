/* Updated EventScreen.kt with SABC theme colors */

package com.example.sabcvasampleapp.presentation.eventdetails

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sabcvasampleapp.resources.EventDetails
import com.example.sabcvasampleapp.resources.Repository
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController, eventId: String) {
    val context = LocalContext.current
    val viewModel: EventViewModel = viewModel()
    val repository = Repository
    val event: EventDetails? by viewModel.event.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    event?.let { e ->
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF430C49), Color(0xFFB00020))))
            ) {

                val imageUrl = "https://picsum.photos/600/300"
                Image(
                    rememberAsyncImagePainter(model = e.image ?: imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp)
                        .background(Color.White, shape = CircleShape)
                        .align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                IconButton(
                    onClick = {
                        val shareText = "Check out this event: ${e.title}\n${Repository.formatEventDateRange(e.startDate, e.endDate)}\n${e.location}"
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Event"))
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp)
                        .background(Color.White, shape = CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = e.title,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(Repository.formatEventDateRange(e.startDate, e.endDate), fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(e.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BadgeWithSquare("${e.attendees.size} attending", Color(0xFFF2F2F2), Color(0xFF388E3C))
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    InfoCardWithIcon(e.location, "Tap for directions", Icons.Default.Place)
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoCardWithIcon(Repository.formatEventDateRange(e.startDate, e.endDate), "Add to calendar", Icons.Default.DateRange)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("About this event", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(e.description, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Attendees", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        val extra = e.attendees.size > 7
                        val attendeeList = e.attendees.take(7).toMutableList()
                        if (extra) attendeeList.add("+${e.attendees.size - 7}")
                        attendeeList.forEach { initial ->
                            val isExtra = initial.startsWith("+")
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(36.dp)
                                    .background(if (isExtra) Color(0xFFF2F2F2) else Color.LightGray, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initial,
                                    color = if (isExtra) Color(0xFF388E3C) else Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if(e.attendees.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Button(
                        onClick = {
                            repository.addAttendee(eventId, "YN")
                            viewModel.loadEvent(eventId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("RSVP Now", fontSize = 16.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    var commentText by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Add a comment...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        trailingIcon = {
                            if (commentText.isNotBlank()) {
                                IconButton(onClick = {
                                    repository.addComment(eventId, commentText.trim())
                                    commentText = ""
                                    viewModel.loadEvent(eventId)
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Send,
                                        contentDescription = "Send",
                                        tint = Color(0xFF2596be)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(30.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    e.comments.forEach {
                        CommentItem(it.name, it.message, it.timestamp)
                    }
                }
            }
        }
    }
}

fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
        hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
        else -> "$days day${if (days == 1L) "" else "s"} ago"
    }
}

@Composable
fun CommentItem(name: String, comment: String, timestamp: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.split(" ").map { it.first() }.joinToString(""),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(comment, fontSize = 14.sp)
                Text(getTimeAgo(timestamp), fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun VerticalOvalIconBox(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(width = 30.dp, height = 40.dp)
            .background(Color(0xFF2596be), shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun InfoCardWithIcon(title: String, subtitle: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF1F1F1), shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalOvalIconBox(icon)

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun BadgeWithSquare(text: String, bgColor: Color, textColor: Color) {
    Row(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)

                .background(textColor, shape = RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = textColor, fontSize = 12.sp)
    }
}