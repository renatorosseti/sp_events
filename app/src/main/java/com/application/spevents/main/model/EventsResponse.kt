package com.application.spevents.main.model

import com.squareup.moshi.Json

 data class Event(
     @Json(name = "id")
     val id: String = "",

     @Json(name = "date")
     val date: String,

     @Json(name = "description")
     val description: String?,

     @Json(name = "location")
     val location: String?,

     @Json(name = "image")
     val image: String?,

     @Json(name = "longitude")
     val longitude: Double?,

     @Json(name = "latitude")
     val latitude: Double?,

     @Json(name = "price")
     val price: String,

     @Json(name = "title")
     val title: String
)