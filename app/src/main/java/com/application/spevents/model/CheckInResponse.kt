package com.application.spevents.model

import com.squareup.moshi.Json

data class CheckInResponse(
    @Json(name = "id")
    val id: String
)