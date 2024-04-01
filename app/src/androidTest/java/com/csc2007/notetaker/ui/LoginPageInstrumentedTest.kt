package com.csc2007.notetaker.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.csc2007.notetaker.ui.login.LoginPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginPageInstrumentedTest {

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
    fun loginPage_CheckUIElementsDisplayed() {
        // Launch the LoginPage
        composeTestRule.activity.setContent {
            LoginPage()
        }

        // Perform UI tests
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Login").assertExists()
        composeTestRule.onNodeWithText("Don't have an Account?").assertExists()
        composeTestRule.onNodeWithText("Create Here").assertExists()
    }

    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalCoilApi::class,
        ExperimentalCoroutinesApi::class
    )
    @Test
    fun loginPage_EnterValidCredentialsAndLogin() {
        // Launch the LoginPage
        composeTestRule.activity.setContent {
            LoginPage()
        }

        // Perform UI tests
        // Enter valid email and password
        composeTestRule.onNodeWithText("Email")
            .performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password")
            .performTextInput("password")

        // Click the Login button
        composeTestRule.onNodeWithText("Login")
            .performClick()

        // Check if the snackbar appears
        composeTestRule.onNodeWithText("Incorrect Email or Password").assertExists()
    }
}
