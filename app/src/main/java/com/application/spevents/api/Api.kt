package com.application.spevents.api

import com.application.spevents.main.model.BookEvent
import com.application.spevents.main.model.Event
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    companion object {
        const val URL = "https://6195587074c1bd00176c6cff.mockapi.io/api/v1/"
    }

    @GET("events")
    fun fetchEvents(): Single<List<Event>>

    @POST("checkin")
    fun requestEventCheckIn(@Body body: BookEvent): Single<BookEvent>
}
