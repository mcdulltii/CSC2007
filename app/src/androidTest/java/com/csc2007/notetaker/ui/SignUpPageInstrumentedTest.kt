package com.csc2007.notetaker.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.csc2007.notetaker.ui.signup.SignUpPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpPageInstrumentedTest {

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
    fun signUpPage_CheckUIElementsDisplayed() {
        // Start the SignUpPage composable
        composeTestRule.activity.setContent {
            SignUpPage()
        }

        // Check if UI elements are displayed
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Username").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Confirm Password").assertExists()
        composeTestRule.onNodeWithText("Register").assertExists()
    }

    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalCoilApi::class,
        ExperimentalCoroutinesApi::class
    )
    @Test
    fun signUpPage_EnterValidDataAndRegister() {
        // Start the SignUpPage composable
        composeTestRule.activity.setContent {
            SignUpPage()
        }

        // Enter valid data
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password")

        // Click the Register button
        composeTestRule.onNodeWithText("Register").performClick()

        // Verify that Snackbar appears with success message
        composeTestRule.onNodeWithText("Successfully created user account").assertExists()
    }
}
