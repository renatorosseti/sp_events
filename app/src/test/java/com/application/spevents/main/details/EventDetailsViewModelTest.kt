package com.application.spevents.main.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.spevents.R
import com.application.spevents.main.book.BookEventViewModel
import com.application.spevents.main.book.BookEventViewState
import com.application.spevents.repository.EventsRepository
import com.application.spevents.model.*
import com.application.spevents.util.NetworkUtil
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class EventDetailsViewModelTest {
    private lateinit var viewModel: BookEventViewModel

    @MockK
    lateinit var eventsRepository: EventsRepository

    @MockK
    lateinit var networkUtil: NetworkUtil

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = BookEventViewModel(eventsRepository, networkUtil)
    }

    @Test
    fun fetchEvents_whenSucceed() {
        val name = "name test"
        val email = "emails test"
        val eventId = "1"
        val bookProfile = BookProfile(eventId = eventId, name = name, email = email)
        val checkInRequest = BookProfile(name = name, email = email, eventId = eventId)
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.requestEventCheckIn(checkInRequest) } returns Single.just(bookProfile)

        val response= viewModel.requestBookEvent(bookProfile)
        Assert.assertEquals("Should return succeed status.", BookEventViewState.ShowBookEventSucceed, response)
    }

    @Test(expected = NetworkException::class)
    fun fetchEvents_whenExceptionOccurs() {
        val name = "name test"
        val email = "emails test"
        val eventId = "1"
        val bookProfile = BookProfile(name = name, email = email, eventId = eventId)
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.requestEventCheckIn(bookProfile) } throws NetworkException(Throwable())

        val response= viewModel.requestBookEvent(bookProfile)
        Assert.assertEquals("Should return ShowRequestError with the error message.", BookEventViewState.ShowNetworkError(
            R.string.error_request), response)
    }

    @Test
    fun fetchEvents_whenInternetIsNotAvailable() {
        val name = "name test"
        val email = "emails test"
        val eventId = "1"
        val bookProfile = BookProfile(name = name, email = email, eventId = eventId)
        every { networkUtil.isInternetAvailable() } returns false

        val response: BookEventViewState? = viewModel.requestBookEvent(bookProfile)

        Assert.assertTrue("Should return ShowRequestError state view.", response is BookEventViewState.ShowNetworkError)

        response as BookEventViewState.ShowNetworkError

        Assert.assertTrue("Should return NoNetworkException.", response.networkException is NoNetworkException)
        Assert.assertEquals("Should return no-network message error.", response.message, R.string.error_internet)
    }
}