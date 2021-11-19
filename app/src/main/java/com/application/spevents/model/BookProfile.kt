package com.application.spevents.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookProfile(
    @Json(name = "eventId")
    val eventId: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String
) : Parcelable