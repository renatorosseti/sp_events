package com.application.spevents.main.events

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.spevents.R
import com.application.spevents.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventsViewModelItTest {

    @get:Rule
    var intentsRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java)

    @Test
    fun fragment_whenIsInstantiated() {
        onView(withId(R.id.loader)).check(matches(isDisplayed()))
    }
}