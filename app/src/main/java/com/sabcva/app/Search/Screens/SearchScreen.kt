package com.sabcva.app.Search.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sabcva.app.Search.ViewModels.Person
import com.sabcva.app.Search.ViewModels.SearchViewModel
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {
    val tabs = listOf("All", "People", "Businesses", "Events")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {

        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(12.dp))
            Text("Search", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        // Search bar
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(selectedTabIndex = viewModel.selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = viewModel.selectedTabIndex == index,
                    onClick = { viewModel.onTabSelected(index) },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter button
        OutlinedButton(onClick = { /* TODO: Implement filter action */ }) {
            Text("Filter")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content
        when (tabs[viewModel.selectedTabIndex]) {
            "People" -> PeopleList(viewModel.filteredPeople)
            else -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No content for '${tabs[viewModel.selectedTabIndex]}' yet.")
            }
        }
    }
}

@Composable
fun PeopleList(people: List<Person>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(people.size) { index ->
            PersonCard(people[index])
        }
    }
}

@Composable
fun PersonCard(person: Person) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = person.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "People", fontSize = 12.sp, color = Color.Gray)
            Text(text = person.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = person.title, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
