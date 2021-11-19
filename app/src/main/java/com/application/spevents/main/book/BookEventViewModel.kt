package com.application.spevents.main.book

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.application.spevents.R
import com.application.spevents.main.EventsRepository
import com.application.spevents.main.model.*
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
                        it.filter { it.eventId == eventId }
                        it.forEach { Log.i(TAG, "Event: ${it.eventId}, email: ${it.email}. ") }
                        response.postValue(BookEventViewState.ShowBooEventProfiles(it))
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

    fun checkProfileFromBookEvent(eventId: String, bookProfile: BookProfile): BookEventViewState? {
        response.value = BookEventViewState.ShowLoadingState

        if (networkUtil.isInternetAvailable()) {
            val eventsDisposable: Disposable = eventsRepository.fetchProfilesFromBookEvent()
                .subscribe(
                    {
                        it.filter { it.eventId == eventId && it.email == bookProfile.email }
                        it.forEach { Log.i(TAG, "Event: ${it.eventId}, email: ${it.email}. ") }
                        val isUserRegisteredForEvent = it.isNotEmpty()
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

        } else {
            response.value = BookEventViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            )
        }

        return response.value
    }

    fun requestCheckIn(bookProfile: BookProfile): BookEventViewState? {
        response.value = BookEventViewState.ShowLoadingState

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