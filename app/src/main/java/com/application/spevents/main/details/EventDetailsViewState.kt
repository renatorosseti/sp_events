package com.application.spevents.main.details

import com.application.spevents.main.model.NetworkException

sealed class EventDetailsViewState {
    object ShowLoadingState: EventDetailsViewState()
    object RefreshEventDetails: EventDetailsViewState()
    object ShowCheckInSucceed: EventDetailsViewState()
    data class ShowNetworkError(
        val message: Int,
        val networkException: NetworkException = NetworkException(Throwable())
    ): EventDetailsViewState()
}