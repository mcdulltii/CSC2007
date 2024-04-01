package com.csc2007.notetaker.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.csc2007.notetaker.database.viewmodel.module.ModuleEvent
import com.csc2007.notetaker.database.viewmodel.module.ModuleState
import com.csc2007.notetaker.ui.module.pages.AddModulePage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddModulePageTest {

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
    fun addModulePage_DisplayedCorrectly() {
        val moduleState = ModuleState(modules = emptyList())
        val onEvent: (ModuleEvent) -> Unit = {}

        // When
        composeTestRule.activity.setContent {
            AddModulePage(onEvent = onEvent, state = moduleState)
        }

        // Then
        composeTestRule.onNodeWithText("Add New Module").assertExists()
        composeTestRule.onNodeWithText("Module Image").assertExists()
        composeTestRule.onNodeWithText("Module Name").assertExists()
        composeTestRule.onNodeWithText("Create Module").assertExists()
        composeTestRule.onNodeWithText("Cancel").assertExists()
    }
}
