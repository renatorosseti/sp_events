package com.application.spevents.main.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.spevents.R
import com.application.spevents.data.Cache.contentFeed
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

class EventsViewModelTest {
    private lateinit var viewModel: EventsViewModel

    @MockK
    lateinit var eventsRepository: EventsRepository

    @MockK
    lateinit var networkUtil: NetworkUtil

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = EventsViewModel(eventsRepository, networkUtil)
    }

    @Test
    fun fetchEvents_whenSucceed() {
        val listResponse: List<Event> = listOf(
            Event(
                id = "id",
                date = "30/11/88",
                location = "location",
                description = "description",
                image = "image",
                longitude = 1.0,
                latitude = 1.0,
                price = "R$ 10,00",
                title = "Event - Test"
            )
        )
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.fetchEvents() } returns Single.just(listResponse)

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowContentFeed(listResponse), response)
    }

    @Test(expected = NetworkException::class)
    fun fetchEvents_whenExceptionOccurs() {
        every { networkUtil.isInternetAvailable() } returns true
        every { eventsRepository.fetchEvents() } throws NetworkException(Throwable())

        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return ShowRequestError with the error message.", EventsViewState.ShowNetworkError(R.string.error_request), response)
    }

    @Test
    fun fetchEvents_whenInternetIsNotAvailable() {
        contentFeed = listOf()
        every { networkUtil.isInternetAvailable() } returns false

        val response: EventsViewState = viewModel.fetchEvents()!!
        print("MESSAGE: $response")
        Assert.assertTrue("Should return ShowRequestError state view.", response is EventsViewState.ShowNetworkError)

        response as EventsViewState.ShowNetworkError

        Assert.assertTrue("Should return NoNetworkException.", response.networkException is NoNetworkException)
        Assert.assertEquals("Should return the no-network message error.", response.message, R.string.error_internet)
    }

    @Test
    fun fetchEvents_whenInternetIsNotAvailableAndListEventsIsCached() {
        contentFeed = listOf(
            Event(
                id = "id",
                date = "30/11/88",
                location = "location",
                description = "description",
                image = "image",
                longitude = 1.0,
                latitude = 1.0,
                price = "R$ 10,00",
                title = "Event - Test"
                )
            )

        every { networkUtil.isInternetAvailable() } returns false
        val response= viewModel.fetchEvents()
        Assert.assertEquals("Should return the expected list when fetch succeed.", EventsViewState.ShowContentFeed(contentFeed), response)
    }
}