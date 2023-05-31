package com.zell.submissionsatufundamental

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class SettingViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val settingPreferences = mock(SettingPreferences::class.java)
    private lateinit var settingViewModel: SettingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        settingViewModel = SettingViewModel(settingPreferences)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test saveThemeSetting`() = testDispatcher.runBlockingTest {
        val isDarkModeActive = true
        settingViewModel.saveThemeSetting(isDarkModeActive)
        verify(settingPreferences).saveThemeSetting(isDarkModeActive)
    }
}