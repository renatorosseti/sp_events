package com.application.spevents.data

import com.application.spevents.model.Event

object Cache {

    var contentFeed = listOf<Event>()

    lateinit var eventDetail: Event
}
