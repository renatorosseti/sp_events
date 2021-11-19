package com.application.spevents.main.events

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.application.spevents.R
import com.application.spevents.data.Cache.contentFeed
import com.application.spevents.repository.EventsRepository
import com.application.spevents.model.HttpCallFailureException
import com.application.spevents.model.NoNetworkException
import com.application.spevents.model.ServerUnreachableException
import com.application.spevents.util.NetworkUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class EventsViewModel(
    private val eventsRepository: EventsRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    var response = MediatorLiveData<EventsViewState>()

    private val disposable = CompositeDisposable()

    private val TAG = EventsViewModel::class.simpleName

    init {
        response.addSource(networkUtil) {
            if (!networkUtil.isInternetAvailable()) {
                response.value = EventsViewState.ShowNetworkError(
                    R.string.error_internet,
                    NoNetworkException(Throwable())
                )
            } else {
                if (contentFeed.isNotEmpty()) {
                    response.value = EventsViewState.ShowContentFeed(contentFeed)
                } else {
                    fetchEvents()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetchEvents() {
        response.value = EventsViewState.ShowLoadingState

        if (!networkUtil.isInternetAvailable()) {
            response.postValue(EventsViewState.ShowNetworkError(
                R.string.error_internet,
                NoNetworkException(Throwable())
            ))
            return
        }

        val eventsDisposable: Disposable = eventsRepository.fetchEvents()
            .subscribe(
                {
                    contentFeed = it
                    response.postValue(EventsViewState.ShowContentFeed(it))
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
                response.value = EventsViewState.ShowNetworkError(R.string.error_internet, error)
                Log.e(TAG, "Internet not available. ${error.message}")
            }
            is ServerUnreachableException -> {
                response.value = EventsViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Server is unreachable. ${error.message}")
            }
            is HttpCallFailureException -> {
                response.value = EventsViewState.ShowNetworkError(R.string.error_request, error)
                Log.e(TAG, "Call failed. ${error.message}")
            }
            else -> {
                response.value = EventsViewState.ShowNetworkError(R.string.error_request)
                Log.e(TAG, "Error: ${error.message}.")
            }
        }
    }
}