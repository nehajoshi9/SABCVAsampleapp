package com.example.sabcva_app.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sabcva_app.screens.search.viewmodel.SearchViewModel

@Composable
fun SearchResultsScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    onHomeClick: () -> Unit
) {
    val categories = listOf("All", "People", "Businesses", "Events")

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val peopleResults by viewModel.peopleResults.collectAsState()
    val businessResults by viewModel.businessResults.collectAsState()
    val eventResults by viewModel.eventResults.collectAsState()

    Scaffold(
        modifier = modifier,
        bottomBar = { BottomNavigationBar(selectedTab = "Search", onHomeClick = onHomeClick) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(paddingValues)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFB00020),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "SABC",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearch(it) },
                placeholder = { Text("Search people, businesses, events…", fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
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

            // Category Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                categories.forEach { category ->
                    TextButton(onClick = { viewModel.selectCategory(category) }) {
                        Text(
                            text = category,
                            color = if (selectedCategory == category) Color(0xFFB00020) else Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                val showSeeAll = selectedCategory == "All"

                if ((selectedCategory == "All" || selectedCategory == "People") && peopleResults.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "People",
                            onSeeAllClick = { viewModel.selectCategory("People") },
                            showSeeAll = showSeeAll
                        )
                    }
                    items(peopleResults) { person ->
                        PersonResultCard(
                            name = person.name ?: "",
                            subtitle = "${person.location ?: ""} • ${person.role ?: ""}",
                            tags = person.tags,
                            color = Color(0xFFB3E5FC)
                        )
                    }
                }

                if ((selectedCategory == "All" || selectedCategory == "Businesses") && businessResults.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Businesses",
                            onSeeAllClick = { viewModel.selectCategory("Businesses") },
                            showSeeAll = showSeeAll
                        )
                    }
                    items(businessResults) { biz ->
                        BusinessResultCard(
                            name = biz.name ?: "",
                            subtitle = "${biz.industry ?: ""} • ${biz.location ?: ""}",
                            description = biz.tagline ?: "",
                            tags = biz.tags,
                            color = Color(0xFFD1C4E9)
                        )
                    }
                }

                if ((selectedCategory == "All" || selectedCategory == "Events") && eventResults.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Events",
                            onSeeAllClick = { viewModel.selectCategory("Events") },
                            showSeeAll = showSeeAll
                        )
                    }
                    items(eventResults) { event ->
                        EventResultCard(
                            title = event.title ?: "",
                            details = event.description ?: "",
                            tags = event.tags
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onHomeClick: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedTab == "Home",
            onClick = { onHomeClick() },
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
            onClick = { /* Already here */ },
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
            onClick = { /* TODO: Navigate to Calendar */ },
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
            onClick = { /* TODO: Navigate to Profile */ },
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

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit, showSeeAll: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        if (showSeeAll) {
            Text(
                text = "See all",
                color = Color(0xFFB00020),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
    }
}

@Composable
fun PersonResultCard(name: String, subtitle: String, tags: List<String>, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    tags.forEach { AssistChip(it) }
                }
            }
        }
    }
}

@Composable
fun BusinessResultCard(
    name: String,
    subtitle: String,
    description: String,
    tags: List<String>,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = Color.Gray, fontSize = 14.sp)
                Text(description, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    tags.forEach { AssistChip(it) }
                }
            }
        }
    }
}

@Composable
fun EventResultCard(title: String, details: String, tags: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 64.dp, height = 40.dp)
                    .background(Color(0xFFDEEAF6), shape = RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(details, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    tags.forEach { AssistChip(it) }
                }
            }
        }
    }
}

@Composable
fun AssistChip(label: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFE0E0E0),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
