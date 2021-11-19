package com.application.spevents.main.details

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.application.spevents.R
import com.application.spevents.main.EventsRepository
import com.application.spevents.main.model.*
import com.application.spevents.util.NetworkUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class EventDetailsViewModel(
    private val eventsRepository: EventsRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    var response = MediatorLiveData<EventDetailsViewState>()

    private val disposable = CompositeDisposable()

    private val TAG = EventDetailsViewModel::class.simpleName

    init {
        response.addSource(networkUtil) {
            if (!networkUtil.isInternetAvailable()) {
                response.value = EventDetailsViewState.ShowNetworkError(
                    R.string.error_internet,
                    NoNetworkException(Throwable())
                )
            } else {
                response.value = EventDetailsViewState.RefreshEventDetails
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun requestCheckIn(eventId: String, name: String, email: String): EventDetailsViewState? {
        response.value = EventDetailsViewState.ShowLoadingState

        if (networkUtil.isInternetAvailable()) {
            var checkInRequest = BookProfile(eventId = eventId, name = name, email = email)
            val eventsDisposable: Disposable = eventsRepository.requestEventCheckIn(checkInRequest)
                .subscribe(
                    {
                        response.value = EventDetailsViewState.ShowCheckInSucceed
                    },
                    {
                        handleError(error = it)
                    }
                )
            disposable.add(eventsDisposable)
        } else {
            response.value = EventDetailsViewState.ShowNetworkError(
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
                    EventDetailsViewState.ShowNetworkError(R.string.error_internet, error)
                Log.e(TAG, "Internet not available. ${error.message}")
            }
            is ServerUnreachableException -> {
                response.value =
                    EventDetailsViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Server is unreachable. ${error.message}")
            }
            is HttpCallFailureException -> {
                response.value =
                    EventDetailsViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Call failed. ${error.message}")
            }
            else -> {
                response.value = EventDetailsViewState.ShowNetworkError(R.string.error_request)
                Log.e(TAG, "Error: ${error.message}.")
            }
        }
    }
}