package com.csc2007.notetaker.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.csc2007.notetaker.ui.settings.SettingsPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

class SettingsPageTest {

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
    fun settingsPage_DisplayedCorrectly() {
        // When
        composeTestRule.activity.setContent {
            SettingsPage()
        }

        // Then
        composeTestRule.onNodeWithText("Account").assertExists()
        composeTestRule.onNodeWithText("Notifications").assertExists()
        composeTestRule.onNodeWithText("Appearance").assertExists()
        composeTestRule.onNodeWithText("Privacy & Security").assertExists()
        composeTestRule.onNodeWithText("Help & Support").assertExists()
        composeTestRule.onNodeWithText("About").assertExists()
    }
}
