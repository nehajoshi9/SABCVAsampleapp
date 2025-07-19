package com.sabcva.app.Search.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sabcva.app.R

data class Person(
    val name: String,
    val title: String,
    val imageRes: Int
)

class SearchViewModel : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var selectedTabIndex by mutableStateOf(1)
        private set

    private val allPeople = listOf(
        Person("Ethan Carter", "Software Engineer at Tech Innovators", R.drawable.business_mixer),
        Person("Sophia Bennett", "Marketing Manager at Global Brands", R.drawable.business_mixer),
        Person("Liam Harper", "Founder of Startup Vision", R.drawable.business_mixer)
    )

    var filteredPeople by mutableStateOf(allPeople)
        private set

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        filteredPeople = allPeople.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.title.contains(query, ignoreCase = true)
        }
    }

    fun onTabSelected(index: Int) {
        selectedTabIndex = index
    }
}
