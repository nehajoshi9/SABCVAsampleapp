package com.example.sabcvasampleapp.neha.presentation.calendar

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.example.sabcvasampleapp.neha.resources.EventDetails
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sabcvasampleapp.neha.presentation.eventdetails.BadgeWithSquare
import com.example.sabcvasampleapp.neha.resources.Repository
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.graphics.Brush
import com.example.sabcvasampleapp.neha.ui.theme.DefaultEventGradients
import java.lang.Math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController) {
    val viewModel: CalendarViewModel = viewModel()
    viewModel.refreshEvents()
    val events by viewModel.events.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val selectedFilter = viewModel.selectedFilter.collectAsState().value
    val currentRange = viewModel.getCurrentDateRange()
    val repository = Repository

    val filteredEvents = events.filter {
            it.title.contains(searchQuery, ignoreCase = true) && Repository.isDateInRangeInclusive(it.startDate, currentRange)
        }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTab = "Calendar") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "SABC Upcoming Events",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black,
                        )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)

            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { viewModel.decrementOffset() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Text(
                    Repository.formatDateRangeString(viewModel.getCurrentDateRange()),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                IconButton(onClick = { viewModel.incrementOffset() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Forward", modifier = Modifier.rotate(180f))
                }
            }



            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search events...", fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    label = "This Week",
                    selected = selectedFilter == "This Week",
                    onClick = { viewModel.updateFilter("This Week") }
                )
                Spacer(modifier = Modifier.width(8.dp))

                FilterChip(
                    label = "Next Week",
                    selected = selectedFilter == "Next Week",
                    onClick = { viewModel.updateFilter("Next Week") }
                )
                Spacer(modifier = Modifier.width(8.dp))

                FilterChip(
                    label = "This Month",
                    selected = selectedFilter == "This Month",
                    onClick = { viewModel.updateFilter("This Month") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredEvents) { event ->
                    EventCard(
                        event = event,
                        onClick = { id ->
                            viewModel.refreshEvents()
                            navController.navigate("event/$id")
                        },
                        onRSVP = {
                            if(!event.attendees.contains("YN")) {
                                repository.addAttendee(event.id, "YN") }
                            else repository.removeAttendee(event.id, "YN")
                            viewModel.loadEvent(event.id)
                            viewModel.refreshEvents()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) Color(0xFFB00020) else Color(0xFFE0E0E0)
    val textColor = if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = label, color = textColor, fontSize = 13.sp)
    }
}

@Composable
fun EventCard(
    event: EventDetails,
    onClick: ((String) -> Unit)? = null,
    onRSVP: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke(event.id) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 🖼 Image with rounded top corners
            val gradientIndex = abs(event.id.hashCode()) % DefaultEventGradients.size
            val fallbackGradient = DefaultEventGradients[gradientIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(fallbackGradient)
            ) {

                if (event.image != null) {
                    Image(
                        rememberAsyncImagePainter(model = event.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    )
                }
            }
            // 💬 Content section with padding
            Column(modifier = Modifier.padding(16.dp)) {
                // 👤 Attendee pill (not touching image)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                ) {
                    BadgeWithSquare("${event.attendees.size} attending", Color(0xFFF2F2F2), Color(0xFF388E3C))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    Repository.formatEventDateRange(event.startDate, event.endDate),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(event.description, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Hosted by ${event.hosts.joinToString()}", fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
            Text(event.location, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRSVP,
                colors = ButtonDefaults.buttonColors(containerColor = if (event.attendees.contains("YN")) Color(0xFF388E3C) else Color(0xFFB00020)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if(event.attendees.contains("YN"))
                    "RSVP'd" else "RSVP Now", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}
    }

@Composable
fun BottomNavigationBar(selectedTab: String) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedTab == "Home",
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB00020),
                selectedTextColor = Color(0xFFB00020),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = selectedTab == "Search",
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB00020),
                selectedTextColor = Color(0xFFB00020),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = selectedTab == "Calendar",
            onClick = { /* already here */ },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
            label = { Text("Calendar") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB00020),
                selectedTextColor = Color(0xFFB00020),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = selectedTab == "Profile",
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB00020),
                selectedTextColor = Color(0xFFB00020),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
