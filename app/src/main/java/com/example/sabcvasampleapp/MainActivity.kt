// --- MainActivity.kt ---
package com.example.sabcvasampleapp

import android.R.style.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SABCVASampleAppTheme {
                CommunityScreen()
            }
        }
    }
}

@Composable
fun CommunityScreen(viewModel: comViewModel = viewModel ()) {
    val selectedFilter = remember { mutableStateOf("All") }
    val showDialog = remember { mutableStateOf(false) }

    val posts = viewModel.getFilteredPosts()
    val selectedFilter = viewModel.selectedFilter

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F7))
                .padding(bottom = 80.dp)
        ) {
            TopBar(onPostClick = { showDialog.value = true })
            FilterChips(selectedFilter.value) { selectedFilter.value = it }
            CreatePostSection(onClick = { showDialog.value = true })

            AnnouncementCard()
            EventCard()
            NewsletterCard()
            SponsoredCard()
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavigationBar()
        }

        if (showDialog.value) {
            PostDialog(onDismiss = { showDialog.value = false })
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
fun PostDialog(onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create a Post") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("What would you like to share?") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
fun AnnouncementCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleAvatar("SA")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("SABC Admin", fontWeight = FontWeight.Bold)
                    Text("Network Director • SABC Business Network")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("2h ago", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF4D6), RoundedCornerShape(8.dp))
                                .padding(horizontal = 3.dp, vertical = 2.dp)
                        ) {
                            Text("Announcement", fontSize = 10.sp, color = Color(0xFFB68B00))
                        }
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 15.dp)
            ){
                //Image(
                  //  painter = painterResource(id = R.drawable.),
                  //  contentDescription = stringResource(id = R.string.maintMess),
                  //  modifier = Modifier.fillMaxSize(),
                  //  contentScale = ContentScale.Crop
               // )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("System Maintenance Notice", fontWeight = FontWeight.Bold)
            Text(
                "Our platform will be undergoing scheduled maintenance this Saturday from 2AM to 5AM. Some features may be temporarily unavailable during this time.",
                fontSize = 14.sp
            )
            Text(
                "Read more",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row {
                IconWithText(Icons.Default.ThumbUp, "24")
                Spacer(modifier = Modifier.width(16.dp))
                //IconWithText(Icons.Default.ChatBubble, "5")
                Spacer(modifier = Modifier.width(16.dp))
                IconWithText(Icons.Default.Share, "Share")
            }
        }
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
fun IconWithText(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 14.sp)
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
fun EventCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.background(Color.White)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6A1B9A))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Annual Business Conference 2023", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("📍 Grand Convention Center", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(Color(0xFF9575CD), shape = RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Text("SEP\n15", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircleAvatar("EV")
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Events Team", fontWeight = FontWeight.Bold)
                        Text("Events Coordinator • SABC Business Network")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("3 days ago", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFD1C4E9), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Event", fontSize = 10.sp, color = Color(0xFF512DA8))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Join us for our annual business conference featuring keynote speakers, networking opportunities, and workshops.")
                Text("Read more", color = Color.Red, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Icon(Icons.Default.Event, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("September 15, 2023", fontWeight = FontWeight.Bold)
                        Text("9:00 AM - 5:00 PM")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { }) {
                        Text("RSVP")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    IconWithText(Icons.Default.ThumbUp, "36")
                    Spacer(modifier = Modifier.width(16.dp))
                    //IconWithText(Icons.Default.ChatBubble, "14")
                    Spacer(modifier = Modifier.width(16.dp))
                    IconWithText(Icons.Default.Share, "Share")
                }
            }
        }
    }
}

@Composable
fun NewsletterCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleAvatar("JD")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("John Doe", fontWeight = FontWeight.Bold)
                    Text("Research Analyst • Global Market Insights")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Yesterday", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF4D6), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Newsletter", fontSize = 10.sp, color = Color(0xFFB68B00))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Quarterly Business Report Released", fontWeight = FontWeight.Bold)
            Text("The Q2 business report has been published. Overall growth exceeded expectations with a 15% increase in network engagement.")
            Text("Read more", color = Color.Red, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { }) {
                //Icon(Icons.Default.FilePresent, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Q2_Business_Report.pdf")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                IconWithText(Icons.Default.ThumbUp, "42")
                Spacer(modifier = Modifier.width(16.dp))
                // IconWithText(Icons.Default.ChatBubble, "12")
                Spacer(modifier = Modifier.width(16.dp))
                IconWithText(Icons.Default.Share, "Share")
            }
        }
    }
}

@Composable
fun SponsoredCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleAvatar("FT")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("FinTech Solutions", fontWeight = FontWeight.Bold)
                    Text("Marketing Director • FinTech Solutions Inc.")
                    Text("Sponsored", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Streamline Your Business Payments", fontWeight = FontWeight.Bold)
            Text("Introducing our new payment processing solution designed for small to medium businesses.")
            Text("Read more", color = Color.Red, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { }) {
                Text("Limited time offer: 30-day free trial")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { }) {
                Text("Learn More")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                IconWithText(Icons.Default.ThumbUp, "18")
                Spacer(modifier = Modifier.width(16.dp))
                // IconWithText(Icons.Default.ChatBubble, "3")
                Spacer(modifier = Modifier.width(16.dp))
                IconWithText(Icons.Default.Share, "Share")
            }
        }
    }
}


