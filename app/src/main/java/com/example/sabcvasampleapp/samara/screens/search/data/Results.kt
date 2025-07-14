package com.example.sabcva_app.screens.search.data

import com.google.gson.annotations.SerializedName


data class Results (

    @SerializedName("people"     ) var people     : ArrayList<People>     = arrayListOf(),
    @SerializedName("businesses" ) var businesses : ArrayList<Businesses> = arrayListOf(),
    @SerializedName("events"     ) var events     : ArrayList<Events>     = arrayListOf()

)