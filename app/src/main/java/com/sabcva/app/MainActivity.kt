package com.sabcva.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sabcva.app.ui.theme.SABCVAAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import coil.compose.AsyncImage
import com.google.firebase.Timestamp

data class Event(
    val dateLabel: String = "",
    val start_time: Timestamp?=null,
    val end_time: Timestamp?=null,
    val location: String= "",
    val title: String= "",
    val description: String= "",
    val imageUrl: String= "",
    var numRSVP: Int = 0
)

//C:\Users\sribs\OneDrive\Personal\SABC\kotlin projects\firstProject\SABCVAApp\app\src\main\java\com\sabcva\app

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SABCVAAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FirestoreEventCalendarScreen()
                }
            }
        }
    }
}

// Sample hardcoded events


//@Composable
//fun EventDetailScreen(event: Event) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text (
//            text = event.title,
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Image(
//            painter = painterResource(id = event.imageResId),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .height(120.dp)
//                .fillMaxWidth()
//                .background(Color.LightGray, RoundedCornerShape(8.dp))
//        )
//    }
//}
@Composable
fun FirestoreEventCard(event: Event) {
    var numRSVP by remember { mutableIntStateOf(event.numRSVP) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Replace this with Coil or Glide image loading from event.imageUrl
            Text(text = "Image: ${event.imageUrl}", fontSize = 12.sp, color = Color.Gray)

            Text(
                text = event.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(text = "${event.start_time} - ${event.end_time}", color = Color.Gray, fontSize = 14.sp)
            Text(text = event.location, color = Color.Gray, fontSize = 14.sp)
            Text(
                text = event.description,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Button(onClick = { numRSVP++ }, shape = RoundedCornerShape(8.dp)) {
                Text("RSVP: $numRSVP")
            }
        }
    }
}

@Composable
fun FirestoreEventCalendarScreen() {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        try {
            val snapshot = db.collection("Events").get().await()
            val loadedEvents = snapshot.toObjects(Event::class.java)
            events = loadedEvents.sortedBy { it.dateLabel } // Sort if needed
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // You can reuse your UI code with EventRemote (or map to Event if needed)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Event",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        itemsIndexed(events) { index, event ->
            val showDateLabel = index == 0 || events[index - 1].dateLabel != event.dateLabel
            Column {
                if (showDateLabel) {
                    Text(
                        text = event.dateLabel,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                FirestoreEventCard(event)
            }
        }
    }
}

@Composable
fun EventCalendarScreen(events: List<Event>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Title section (not scrollable)
        Text(
            text = "Events",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Scrollable event list
        LazyColumn(
            modifier = Modifier.fillMaxSize(), // takes all remaining space and scrolls
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(events) { index, event ->
                val showDateLabel = index == 0 || events[index - 1].dateLabel != event.dateLabel
                Column {
                    if (showDateLabel) {
                        Text(
                            text = event.dateLabel,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    EventCard(event)
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onTitleClick: () -> Unit = {}) {
    var numRSVP by remember { mutableIntStateOf(event.numRSVP) }
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable(onClick = onTitleClick)
                        .padding(vertical = 4.dp)
                )
                Text(text = "${event.start_time} - ${event.end_time}", color = Color.Gray, fontSize = 14.sp)
                Text(text = event.location, color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = event.description,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Button(onClick = { numRSVP++ }, shape = RoundedCornerShape(8.dp)) {
                    Text("RSVP: ${numRSVP}")
                }
            }
        }
    }
}
