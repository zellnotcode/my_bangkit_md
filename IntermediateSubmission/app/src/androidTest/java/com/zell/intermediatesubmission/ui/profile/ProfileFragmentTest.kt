package com.zell.intermediatesubmission.ui.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.zell.intermediatesubmission.EspressoIdlingResource
import com.zell.intermediatesubmission.R
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProfileFragmentTest {

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLogout() {
        launchFragmentInContainer<ProfileFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_logout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.btn_logout)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.authFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}