package com.application.spevents.main.events

import com.application.spevents.model.Event
import com.application.spevents.model.NetworkException

sealed class EventsViewState {
    object ShowLoadingState: EventsViewState()
    data class ShowContentFeed(val events: List<Event>): EventsViewState()
    data class ShowNetworkError(val message: Int, var networkException: NetworkException = NetworkException(Throwable())): EventsViewState()
}