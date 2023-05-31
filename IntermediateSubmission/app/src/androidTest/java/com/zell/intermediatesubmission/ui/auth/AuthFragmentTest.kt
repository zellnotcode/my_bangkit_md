@file:Suppress("DEPRECATION")

package com.zell.intermediatesubmission.ui.auth

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.zell.intermediatesubmission.IdlingResourceHelper
import com.zell.intermediatesubmission.MainActivity
import com.zell.intermediatesubmission.R
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthFragmentTest {

    @JvmField
    @Rule
    val fragmentTestRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<AuthFragment>

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        val mockBaseUrl = mockWebServer.url("/").toString()
        IdlingResourceHelper.baseUrl = mockBaseUrl

        scenario = launchFragmentInContainer()
        scenario.onFragment {
            navController = Navigation.findNavController(it.requireView())
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testLoginSuccess() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{'token' : '123abc'"))

        onView(withId(R.id.ed_login_email)).perform(typeText("test@gmail.com"))
        onView(withId(R.id.ed_login_password)).perform(typeText("password"))
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).check(matches(isNotEnabled()))

        IdlingResourceHelper.waitForIdle()

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))

        assert(navController.currentDestination?.id == R.id.homeFragment)
    }
}