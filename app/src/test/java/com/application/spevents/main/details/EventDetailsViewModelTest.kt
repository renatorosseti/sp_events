package com.application.spevents.main.details

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.spevents.R
import com.application.spevents.data.Cache.eventDetail
import com.application.spevents.main.EventsRepository
import com.application.spevents.main.model.*
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
    private lateinit var viewModel: EventDetailsViewModel

    @MockK
    lateinit var eventsRepository: EventsRepository

    @MockK
    lateinit var networkUtil: NetworkUtil

    @MockK
    lateinit var bundle: Bundle

    @MockK
    lateinit var context: Context

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = EventDetailsViewModel(eventsRepository, networkUtil)
    }

    @Test
    fun fetchEvents_whenSucceed() {
        val name = "name test"
        val email = "emails test"
        val eventId = "1"
        val checkInRequest = BookEvent(name = name, email = email, eventId = eventId)
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.requestEventCheckIn(checkInRequest) } returns Single.just(CheckInResponse("1"))

        val response= viewModel.requestCheckIn(eventId = eventId, name = name, email = email)
        Assert.assertEquals("Should return succeed status.", EventDetailsViewState.ShowCheckInSucceed, response)
    }

    @Test(expected = NetworkException::class)
    fun fetchEvents_whenExceptionOccurs() {
        val name = "name test"
        val email = "emails test"
        val eventId = "1"
        val checkInRequest = BookEvent(name = name, email = email, eventId = eventId)
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.requestEventCheckIn(checkInRequest) } throws NetworkException(Throwable())

        val response= viewModel.requestCheckIn(eventId = eventId, name = name, email = email)
        Assert.assertEquals("Should return ShowRequestError with the error message.", EventDetailsViewState.ShowNetworkError(
            R.string.error_request), response)
    }

    @Test
    fun fetchEvents_whenInternetIsNotAvailable() {
        val name = "name test"
        val email = "emails test"
        val eventId = "1"
        every { networkUtil.isInternetAvailable() } returns false

        val response: EventDetailsViewState? = viewModel.requestCheckIn(eventId = eventId, name = name, email = email)

        Assert.assertTrue("Should return ShowRequestError state view.", response is EventDetailsViewState.ShowNetworkError)

        response as EventDetailsViewState.ShowNetworkError

        Assert.assertTrue("Should return NoNetworkException.", response.networkException is NoNetworkException)
        Assert.assertEquals("Should return no-network message error.", response.message, R.string.error_internet)
    }

    @Test
    fun handleBundleData_whenCheckInFormIsFilled() {
        val name = "name test"
        val email = "emails test"

        eventDetail = Event(
            people = listOf(Person()),
            date = 1.0,
            description = "description",
            image = "image",
            longitude = 1.0,
            latitude = 1.0,
            price = 1.0,
            title = "Event - Test",
            id = "id",
            coupons = listOf(Coupon())
        )

        val checkInRequest = BookEvent(name = name, email = email, eventId = "id")
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.requestEventCheckIn(checkInRequest) } returns Single.just(CheckInResponse("1"))
        every { bundle.getBoolean("checkIn") } returns true
        every { bundle.getString("name") } returns name
        every { bundle.getString("email") } returns email
        every { bundle.clear() } returns Unit

        val response = viewModel.handleBundleData(bundle)
        Assert.assertEquals("Should return ShowCheckInSucceed status when check-in form is filled up.", EventDetailsViewState.ShowCheckInSucceed, response)
    }

    @Test
    fun handleBundleData_whenCheckInFormIsNotFilled() {
        every { bundle.getBoolean("checkIn") } returns false

        val response = viewModel.handleBundleData(bundle)
        Assert.assertEquals("Should return UpdateEventDetails status when check-in form is not filled up..", EventDetailsViewState.RefreshEventDetails, response)
    }

    @Test
    fun navigateToSharingEvent_whenInternetIsNotAvailable() {
        val event = Event(
            people = listOf(Person()),
            date = 1.0,
            description = "description",
            image = "image",
            longitude = 1.0,
            latitude = 1.0,
            price = 1.0,
            title = "Event - Test",
            id = "id",
            coupons = listOf(Coupon())
        )
        every { networkUtil.isInternetAvailable() } returns false

        val response = viewModel.navigateToSharingEvent(context, event)

        Assert.assertTrue("Should return ShowRequestError state view.", response is EventDetailsViewState.ShowNetworkError)

        response as EventDetailsViewState.ShowNetworkError
        Assert.assertTrue("Should return NoNetworkException.", response.networkException is NoNetworkException)
        Assert.assertEquals("Should return no-network message error.", response.message, R.string.error_internet)
    }
}