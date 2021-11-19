package com.application.spevents.main.book

import com.application.spevents.main.model.BookProfile
import com.application.spevents.main.model.NetworkException

sealed class BookEventViewState {
    object ShowLoadingState: BookEventViewState()
    object ShowBookEventSucceed: BookEventViewState()
    data class ShowBooEventProfiles(val events: List<BookProfile>): BookEventViewState()
    object RefreshEventDetails: BookEventViewState()
    data class CheckProfileOnEvent(val isUserRegistered: Boolean, val bookProfile: BookProfile): BookEventViewState()
    data class ShowNetworkError(
        val message: Int,
        val networkException: NetworkException = NetworkException(Throwable())
    ): BookEventViewState()
}