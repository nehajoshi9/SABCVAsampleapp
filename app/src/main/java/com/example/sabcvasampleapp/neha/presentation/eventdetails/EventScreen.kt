/* Updated EventScreen.kt with SABC theme colors */

package com.example.sabcvasampleapp.neha.presentation.eventdetails

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sabcvasampleapp.neha.resources.EventDetails
import com.example.sabcvasampleapp.neha.resources.Repository
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.documentfile.provider.DocumentFile
import coil.compose.rememberAsyncImagePainter
import com.example.sabcvasampleapp.neha.ui.theme.BubbleColors
import com.example.sabcvasampleapp.neha.ui.theme.DefaultEventGradients
import java.lang.Math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController, eventId: String) {
    val context = LocalContext.current
    val viewModel: EventViewModel = viewModel()
    val repository = Repository
    val event: EventDetails? by viewModel.event.collectAsState()

    val showFilePreview = remember { mutableStateOf(false) }
    val selectedFileUriState = remember { mutableStateOf<Uri?>(null) }
    var fn = "the attached file."

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    if (showFilePreview.value && selectedFileUriState.value != null) {
        val uri = selectedFileUriState.value
        if (uri != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, context.contentResolver.getType(uri))
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            showFilePreview.value = false
            selectedFileUriState.value = null
        }
/* // "Are you sure you want to open the file"
        ModalBottomSheet(
            onDismissRequest = { showFilePreview.value = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Attachment", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text("Tap below to open ${fn}")

                Spacer(Modifier.height(24.dp))
                Row() {
                    Button(onClick = {
                        val uri = selectedFileUriState.value
                        if (uri != null) {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, context.contentResolver.getType(uri))
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        }
                    }) {
                        Text("Open File")
                    }

                    Spacer(Modifier.width(18.dp))
                    TextButton(onClick = { showFilePreview.value = false }) {
                        Text("Close")
                    }
                }
            }
        } */
    }


    event?.let { e ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            val gradientIndex = abs(e.id.hashCode()) % DefaultEventGradients.size
            val fallbackGradient = DefaultEventGradients[gradientIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(fallbackGradient)
            ) {

                if (e.fileUri != null) {
                Image(
                    rememberAsyncImagePainter(model = e.fileUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                ) }

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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
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

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Attendees", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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
                                    .background(
                                        if (isExtra) Color(0xFFF2F2F2) else Color.LightGray,
                                        shape = CircleShape
                                    ),
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

                    Text("Hosts", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(e.hosts) { host ->
                            HostCard(
                                name = host.name,
                                isBusiness = host.type === "Business",
                                imageUri = host.profilePicture,
                                color = BubbleColors[abs(host.id.hashCode()) % BubbleColors.size]
                            )
                        }
                    }

                    if(e.sponsors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sponsors", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(e.sponsors) { sponsor ->
                                HostCard(
                                    name = sponsor.name,
                                    isBusiness = true,
                                    imageUri = null,
                                    color = BubbleColors[abs(sponsor.id.hashCode()) % BubbleColors.size]
                                )
                            }
                        }
                    }
                    if(e.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Tags", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        TagSection(e.tags, navController)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if(!e.attendees.contains("YN")) {
                            repository.addAttendee(eventId, "YN") }
                            else repository.removeAttendee(eventId, "YN")
                            viewModel.loadEvent(eventId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (e.attendees.contains("YN")) Color(0xFF388E3C) else Color(0xFFB00020)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if(e.attendees.contains("YN"))
                            "RSVP'd" else "RSVP Now", fontSize = 16.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if(!e.hasImageFile && e.fileUri != null) {
                        Text("Attachment", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        val fileName = remember(e.fileUri) {
                            DocumentFile.fromSingleUri(context, e.fileUri)?.name ?: "Unnamed File"
                        }
                        if(fileName !== "Unnamed File") {
                            fn = fileName
                        }
                        FilePreview(uri=e.fileUri, context=context, fileName=fileName) {
                            selectedFileUriState.value = e.fileUri
                            showFilePreview.value = true
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

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

@Composable
fun HostCard(name: String, isBusiness: Boolean, imageUri: Uri?, color: Color = Color(0xFFB00020)) {
    Box(modifier = Modifier
        .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(12.dp))
        .padding(16.dp)
        .height(100.dp)
        .clickable { // navigate to their profile
             }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(100.dp)
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(49.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(49.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.split(" ").map { it.first().uppercase() }.joinToString(""),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = name.capitalize(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (isBusiness) "Business" else "Person",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSection(tags: List<String>, navController: NavController) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable {
                        navController.navigate("calendar/search=$tag")
                    }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocalOffer,
                        contentDescription = "Tag Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = tag,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun FilePreview(uri: Uri?, context: Context, fileName: String, onClick: () -> Unit) {
    if (uri == null) return

    val mimeType = remember(uri) {
        uri?.let { context.contentResolver.getType(it) }
    }

    val icon = when {
        mimeType?.startsWith("image/") == true -> Icons.Default.Image
        mimeType == "application/pdf" -> Icons.Default.PictureAsPdf
        mimeType?.startsWith("text/") == true -> Icons.Default.Article
        mimeType?.contains("excel", ignoreCase = true) == true -> Icons.Default.GridOn
        mimeType?.contains("word", ignoreCase = true) == true -> Icons.Default.Description
        mimeType?.contains("powerpoint", ignoreCase = true) == true -> Icons.Default.Slideshow // optional
        else -> Icons.Default.InsertDriveFile
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(fileName, fontSize = 14.sp)
        }
    }
}