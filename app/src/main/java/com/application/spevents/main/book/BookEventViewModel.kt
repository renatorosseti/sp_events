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
                    val profiles = it.filter { it.eventId == eventId }
                    response.postValue(BookEventViewState.ShowBookEventProfiles(profiles))
                },
                {
                    handleError(error = it)
                }
            )
        disposable.add(eventsDisposable)

        return response.value
    }

    fun verifyProfileFromBookEvent(eventId: String, bookProfile: BookProfile) {
        response.postValue(BookEventViewState.ShowLoadingState)

        if (bookProfile.email.isEmpty() || bookProfile.name.isEmpty()) {
            response.postValue(BookEventViewState.ShowErrorEmptyData)
            return
        }

        if (!networkUtil.isInternetAvailable()) {
            response.postValue(BookEventViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            ))
            return
        }

        val eventsDisposable: Disposable = eventsRepository.fetchProfilesFromBookEvent()
            .subscribe(
                {
                    val profiles =
                        it.filter { it.eventId == eventId && it.email == bookProfile.email }
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
    }

    fun requestBookEvent(bookProfile: BookProfile) {
        response.postValue(BookEventViewState.ShowLoadingState)

        if (!networkUtil.isInternetAvailable()) {
            response.postValue(BookEventViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            ))
            return
        }

        val eventsDisposable: Disposable = eventsRepository.requestEventCheckIn(bookProfile)
            .subscribe(
                {
                    response.postValue(BookEventViewState.ShowBookEventSucceed)
                },
                {
                    handleError(error = it)
                }
            )
        disposable.add(eventsDisposable)
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