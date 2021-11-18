package com.application.spevents.main.model

import com.squareup.moshi.Json

data class BookEvent(
    @Json(name = "eventId")
    val eventId: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String
)