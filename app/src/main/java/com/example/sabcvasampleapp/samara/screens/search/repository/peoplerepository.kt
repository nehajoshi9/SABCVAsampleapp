package com.example.sabcva_app.screens.search.repository

import com.example.sabcva_app.screens.search.data.People

class peoplerepository {

    private val peopleList = mutableListOf(
        People(id = "1", name = "Priya Sharma", location = "Richmond, VA", role = "CEO", tags = arrayListOf("Entrepreneurship", "Mobile Apps")),
        People(id = "2", name = "Raj Patel", location = "Alexandria, VA", role = "Developer", tags = arrayListOf("Technology", "AI"))
    )

    // Read
    fun getPeople(): List<People> = peopleList.toList()

    // Create
    fun addPerson(person: People) {
        peopleList.add(person)
    }

    // Update
    fun updatePerson(updatedPerson: People) {
        val index = peopleList.indexOfFirst { it.id == updatedPerson.id }
        if (index != -1) {
            peopleList[index] = updatedPerson
        }
    }

    // Delete
    fun deletePerson(personId: String) {
        peopleList.removeAll { it.id == personId }
    }
}
