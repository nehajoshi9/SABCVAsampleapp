package com.example.sabcva_app.screens.search.data

import com.google.gson.annotations.SerializedName


data class Events (

  @SerializedName("event_id"    ) var eventId     : String?           = null,
  @SerializedName("title"       ) var title       : String?           = null,
  @SerializedName("date"        ) var date        : String?           = null,
  @SerializedName("time"        ) var time        : String?           = null,
  @SerializedName("location"    ) var location    : String?           = null,
  @SerializedName("description" ) var description : String?           = null,
  @SerializedName("tags"        ) var tags        : ArrayList<String> = arrayListOf()

)