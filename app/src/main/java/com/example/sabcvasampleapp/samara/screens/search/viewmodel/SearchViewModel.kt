package com.example.sabcva_app.screens.search.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.sabcva_app.screens.search.data.People
import com.example.sabcva_app.screens.search.data.Businesses
import com.example.sabcva_app.screens.search.data.Events
import com.example.sabcva_app.screens.search.repository.peoplerepository
import com.example.sabcva_app.screens.search.repository.businessrepository
import com.example.sabcva_app.screens.search.repository.eventrepository

class SearchViewModel : ViewModel() {

    private val peopleRepo = peoplerepository()
    private val businessRepo = businessrepository()
    private val eventsRepo = eventrepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _peopleResults = MutableStateFlow<List<People>>(emptyList())
    val peopleResults: StateFlow<List<People>> = _peopleResults

    private val _businessResults = MutableStateFlow<List<Businesses>>(emptyList())
    val businessResults: StateFlow<List<Businesses>> = _businessResults

    private val _eventResults = MutableStateFlow<List<Events>>(emptyList())
    val eventResults: StateFlow<List<Events>> = _eventResults

    init {
        updateResults()
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
        updateResults()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        updateResults()
    }

    private fun updateResults() {
        val query = _searchQuery.value

        _peopleResults.value = peopleRepo.getPeople().filter {
            query.isBlank() ||
                    it.name?.contains(query, ignoreCase = true) == true ||
                    it.location?.contains(query, ignoreCase = true) == true ||
                    it.role?.contains(query, ignoreCase = true) == true ||
                    it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }

        _businessResults.value = businessRepo.getBusinesses().filter {
            query.isBlank() ||
                    it.name?.contains(query, ignoreCase = true) == true ||
                    it.industry?.contains(query, ignoreCase = true) == true ||
                    it.location?.contains(query, ignoreCase = true) == true ||
                    it.tagline?.contains(query, ignoreCase = true) == true ||
                    it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }

        _eventResults.value = eventsRepo.getEvents().filter {
            query.isBlank() ||
                    it.title?.contains(query, ignoreCase = true) == true ||
                    it.description?.contains(query, ignoreCase = true) == true ||
                    it.location?.contains(query, ignoreCase = true) == true ||
                    it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
    // PEOPLE
    fun addPerson(person: People) {
        peopleRepo.addPerson(person)
        updateResults()
    }

    fun updatePerson(person: People) {
        peopleRepo.updatePerson(person)
        updateResults()
    }

    fun deletePerson(personId: String) {
        peopleRepo.deletePerson(personId)
        updateResults()
    }

    // BUSINESSES
    fun addBusiness(business: Businesses) {
        businessRepo.addBusiness(business)
        updateResults()
    }

    fun updateBusiness(business: Businesses) {
        businessRepo.updateBusiness(business)
        updateResults()
    }

    fun deleteBusiness(businessId: String) {
        businessRepo.deleteBusiness(businessId)
        updateResults()
    }

    // EVENTS
    fun addEvent(event: Events) {
        eventsRepo.addEvent(event)
        updateResults()
    }

    fun updateEvent(event: Events) {
        eventsRepo.updateEvent(event)
        updateResults()
    }

    fun deleteEvent(eventId: String) {
        eventsRepo.deleteEvent(eventId)
        updateResults()
    }

}

