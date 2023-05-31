package com.zell.intermediatesubmission

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object IdlingResourceHelper {

    private const val RESOURCE = "GLOBAL"

    private val countingIdlingResource = CountingIdlingResource(RESOURCE)

    var baseUrl: String = ""
        set(value) {
            field = value
            countingIdlingResource.increment()
        }

    fun getIdlingResource() : IdlingResource {
        return countingIdlingResource
    }

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }

    fun waitForIdle() {
        if(!countingIdlingResource.isIdleNow) {
            IdlingRegistry.getInstance().register(countingIdlingResource)
            countingIdlingResource.decrement()
            IdlingRegistry.getInstance().register(countingIdlingResource)
        }
    }
}