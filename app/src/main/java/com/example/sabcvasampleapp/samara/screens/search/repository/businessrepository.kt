package com.example.sabcva_app.screens.search.repository

import com.example.sabcva_app.screens.search.data.Businesses

class businessrepository {

    private val businessList = mutableListOf(
        Businesses(businessId = "1", name = "Spice Junction", industry = "Restaurant", location = "Norfolk, VA", tagline = "Authentic South Asian cuisine with modern twists.", tags = arrayListOf("Food", "Catering", "Restaurant")),
        Businesses(businessId = "2", name = "TechVision Solutions", industry = "Tech Startup", location = "Fairfax, VA", tagline = "AI-driven solutions for modern enterprises.", tags = arrayListOf("AI", "Software"))
    )

    // Read
    fun getBusinesses(): List<Businesses> = businessList.toList()

    // Create
    fun addBusiness(business: Businesses) {
        businessList.add(business)
    }

    // Update
    fun updateBusiness(updatedBusiness: Businesses) {
        val index = businessList.indexOfFirst { it.businessId == updatedBusiness.businessId }
        if (index != -1) {
            businessList[index] = updatedBusiness
        }
    }

    // Delete
    fun deleteBusiness(businessId: String) {
        businessList.removeAll { it.businessId == businessId }
    }
}
