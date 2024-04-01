package com.csc2007.notetaker.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LandingPageInstrumentedTest {

    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalCoilApi::class,
        ExperimentalCoroutinesApi::class
    )
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalCoilApi::class,
        ExperimentalCoroutinesApi::class
    )
    @Test
    fun landingPage_CheckButtonsTextDisplayed() {
        // Check if "Login" and "Register" buttons are displayed
        composeTestRule.onNodeWithText("Login").assertExists()
        composeTestRule.onNodeWithText("Register").assertExists()
    }
}
