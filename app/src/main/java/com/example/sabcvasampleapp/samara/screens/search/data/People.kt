package com.example.sabcva_app.screens.search.data

import com.google.gson.annotations.SerializedName


data class People (

  @SerializedName("id"       ) var id       : String?           = null,
  @SerializedName("name"     ) var name     : String?           = null,
  @SerializedName("location" ) var location : String?           = null,
  @SerializedName("role"     ) var role     : String?           = null,
  @SerializedName("tags"     ) var tags     : ArrayList<String> = arrayListOf()

)