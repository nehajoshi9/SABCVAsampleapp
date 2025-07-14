package com.example.sabcva_app.screens.search.data

import com.google.gson.annotations.SerializedName


data class Businesses (

  @SerializedName("business_id" ) var businessId : String?           = null,
  @SerializedName("name"        ) var name       : String?           = null,
  @SerializedName("industry"    ) var industry   : String?           = null,
  @SerializedName("location"    ) var location   : String?           = null,
  @SerializedName("tagline"     ) var tagline    : String?           = null,
  @SerializedName("tags"        ) var tags       : ArrayList<String> = arrayListOf()

)