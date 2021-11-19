package com.application.spevents.main.book

import com.application.spevents.model.BookProfile
import com.application.spevents.model.NetworkException

sealed class BookEventViewState {
    object ShowLoadingState: BookEventViewState()
    object ShowBookEventSucceed: BookEventViewState()
    object RefreshEventDetails: BookEventViewState()
    object ShowErrorEmptyData: BookEventViewState()
    data class ShowBookEventProfiles(val profiles: List<BookProfile>): BookEventViewState()
    data class CheckProfileOnEvent(val isUserRegistered: Boolean, val bookProfile: BookProfile): BookEventViewState()
    data class ShowNetworkError(
        val message: Int,
        val networkException: NetworkException = NetworkException(Throwable())
    ): BookEventViewState()
}