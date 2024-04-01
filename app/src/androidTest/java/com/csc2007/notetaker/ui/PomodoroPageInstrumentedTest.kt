package com.csc2007.notetaker.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.csc2007.notetaker.ui.pomodoro.PomodoroPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PomodoroPageInstrumentedTest {

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
    fun pomodoroPage_CheckUIElementsDisplayed() {
        // Launch the PomodoroPage
        composeTestRule.activity.setContent {
            PomodoroPage()
        }

        // Perform UI tests
        composeTestRule.onNodeWithText("Pomodoro Timer").assertExists()
        composeTestRule.onNodeWithText("Start").assertExists()
        composeTestRule.onNodeWithText("Pause").assertDoesNotExist()
        composeTestRule.onNodeWithText("Restart").assertExists()
        composeTestRule.onNodeWithText("Short Break").assertExists()
        composeTestRule.onNodeWithText("Long Break").assertExists()
    }

    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalCoilApi::class,
        ExperimentalCoroutinesApi::class
    )
    @Test
    fun pomodoroPage_StartButton_Click() {
        // Launch the PomodoroPage
        composeTestRule.activity.setContent {
            PomodoroPage()
        }

        // Perform UI test: Click the Start button
        composeTestRule.onNodeWithText("Start").performClick()

        // Check if the Pause button appears after clicking Start
        composeTestRule.onNodeWithText("Pause").assertExists()
    }
}
