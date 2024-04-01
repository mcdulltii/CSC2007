package com.csc2007.notetaker.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.MainActivity
import com.csc2007.notetaker.database.Module
import com.csc2007.notetaker.database.viewmodel.module.ModuleEvent
import com.csc2007.notetaker.database.viewmodel.module.ModuleState
import com.csc2007.notetaker.ui.module.pages.ModulesPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModulesPageTest {

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
    fun modulesPage_DisplayedCorrectly() {
        val moduleState = ModuleState(modules = listOf(Module(1, "Module 1", System.currentTimeMillis(), ""), Module(2, "Module 2", System.currentTimeMillis(), "")))
        val onEvent: (ModuleEvent) -> Unit = {}

        // When
        composeTestRule.activity.setContent {
            ModulesPage(state = moduleState, onEvent = onEvent)
        }

        // Then
        composeTestRule.onNodeWithText("Module 1").assertExists()
        composeTestRule.onNodeWithText("Module 2").assertExists()
        composeTestRule.onNodeWithText("No modules yet.").assertDoesNotExist()
    }
}
