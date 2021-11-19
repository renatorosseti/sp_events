package com.application.spevents.main.book

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.application.spevents.R
import com.application.spevents.repository.EventsRepository
import com.application.spevents.model.*
import com.application.spevents.util.NetworkUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class BookEventViewModel(
    private val eventsRepository: EventsRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    var response = MediatorLiveData<BookEventViewState>()

    private val disposable = CompositeDisposable()

    private val TAG = BookEventViewModel::class.simpleName

    init {
        response.addSource(networkUtil) {
            if (!networkUtil.isInternetAvailable()) {
                response.value = BookEventViewState.ShowNetworkError(
                    R.string.error_internet,
                    NoNetworkException(Throwable())
                )
            } else {
                response.value = BookEventViewState.RefreshEventDetails
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetchProfilesFromBookEvent(eventId: String): BookEventViewState? {
        response.value = BookEventViewState.ShowLoadingState

        if (networkUtil.isInternetAvailable()) {
            val eventsDisposable: Disposable = eventsRepository.fetchProfilesFromBookEvent()
                .subscribe(
                    {
                        it.forEach { Log.i("BookEventViewState", "eventId: ${it.eventId}") }
                        val profiles = it.filter { it.eventId == eventId }

                        Log.i("BookEventViewState", "eventId: $eventId")
                        Log.i("BookEventViewState", "Profiles: $profiles")
                        response.postValue(BookEventViewState.ShowBookEventProfiles(profiles))
                    },
                    {
                        handleError(error = it)
                    }
                )
            disposable.add(eventsDisposable)

        } else {
            response.value = BookEventViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            )
        }

        return response.value
    }

    fun verifyProfileFromBookEvent(eventId: String, bookProfile: BookProfile): BookEventViewState? {
        response.postValue(BookEventViewState.ShowLoadingState)

        if (bookProfile.email.isEmpty() || bookProfile.name.isEmpty()) {
            response.postValue(BookEventViewState.ShowErrorEmptyData)
            return response.value
        }

        if (!networkUtil.isInternetAvailable()) {
            response.value = BookEventViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            )
            return response.value
        }


        val eventsDisposable: Disposable = eventsRepository.fetchProfilesFromBookEvent()
            .subscribe(
                {
                    val profiles = it.filter { it.eventId == eventId && it.email == bookProfile.email }
                    it.forEach { Log.i(TAG, "Event: ${it.eventId}, email: ${it.email}. ") }
                    val isUserRegisteredForEvent = profiles.isNotEmpty()
                    response.postValue(
                        BookEventViewState.CheckProfileOnEvent(
                            isUserRegisteredForEvent,
                            bookProfile
                        )
                    )
                },
                {
                    handleError(error = it)
                }
            )
        disposable.add(eventsDisposable)

        return response.value
    }

    fun requestBookEvent(bookProfile: BookProfile): BookEventViewState? {
        response.value = BookEventViewState.ShowLoadingState
        Log.i(TAG, "BookProfile: ${bookProfile.eventId}")

        if (networkUtil.isInternetAvailable()) {
            val eventsDisposable: Disposable = eventsRepository.requestEventCheckIn(bookProfile)
                .subscribe(
                    {
                        response.value = BookEventViewState.ShowBookEventSucceed
                    },
                    {
                        handleError(error = it)
                    }
                )
            disposable.add(eventsDisposable)
        } else {
            response.value = BookEventViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            )
        }

        return response.value
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is NoNetworkException -> {
                response.value =
                    BookEventViewState.ShowNetworkError(R.string.error_internet, error)
                Log.e(TAG, "Internet not available. ${error.message}")
            }
            is ServerUnreachableException -> {
                response.value =
                    BookEventViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Server is unreachable. ${error.message}")
            }
            is HttpCallFailureException -> {
                response.value =
                    BookEventViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Call failed. ${error.message}")
            }
            else -> {
                response.value = BookEventViewState.ShowNetworkError(R.string.error_request)
                Log.e(TAG, "Error: ${error.message}.")
            }
        }
    }
}