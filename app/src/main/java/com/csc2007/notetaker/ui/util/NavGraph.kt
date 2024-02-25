package com.csc2007.notetaker.ui.util

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.database.viewmodel.PomodoroTimerViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModel
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModelFactory
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModel
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModelFactory
import com.csc2007.notetaker.ui.LandingPage
import com.csc2007.notetaker.ui.avatar.AvatarPage
import com.csc2007.notetaker.ui.camera.CameraPage
import com.csc2007.notetaker.ui.chat.ChatPage
import com.csc2007.notetaker.ui.login.LoginPage
import com.csc2007.notetaker.ui.microphone.MicrophonePage
import com.csc2007.notetaker.ui.microphone.VoiceToTextParser
import com.csc2007.notetaker.ui.module.pages.AddModulePage
import com.csc2007.notetaker.ui.module.pages.ModulesPage
import com.csc2007.notetaker.ui.note.pages.NotePage
import com.csc2007.notetaker.ui.note.pages.NotesPage
import com.csc2007.notetaker.ui.note.pages.AddNotePage
import com.csc2007.notetaker.ui.pomodoro.PomodoroPage
import com.csc2007.notetaker.ui.settings.PomodoroSettingsPage
import com.csc2007.notetaker.ui.settings.AccountSettingsPage
import com.csc2007.notetaker.ui.settings.NotificationsSettingsPage
import com.csc2007.notetaker.ui.settings.SettingsPage
import com.csc2007.notetaker.ui.signup.SignUpPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.StickyNote
import compose.icons.fontawesomeicons.solid.Camera
import kotlinx.coroutines.ExperimentalCoroutinesApi


sealed class Screens(val route: String, val title: String? = null, val icon: ImageVector? = null) {

    object LandingScreen : Screens(route = "landing_screen")
    object SignUpScreen : Screens(route = "signup_screen")
    object LoginScreen : Screens(route = "login_screen")
    object CameraScreen :
        Screens(route = "camera_screen", icon = FontAwesomeIcons.Solid.Camera, title = "Camera")

    object MicrophoneScreen :
        Screens(route = "microphone_screen", icon = Icons.Default.Mic, title = "Microphone")

    object NotesScreen :
        Screens(route = "notes_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "Notes")

    object NoteScreen : Screens(route = "note_screen")

    object AddNoteScreen : Screens(route = "add_note_screen")

    object AddModuleScreen : Screens(route = "add_module_screen")

    object ModulesScreen :
        Screens(
            route = "modules_screen",
            icon = FontAwesomeIcons.Regular.StickyNote,
            title = "Modules"
        )


    object ChatScreen :
        Screens(route = "chat_screen", icon = Icons.Default.ChatBubbleOutline, title = "Chat")

    object PomodoroScreen :
        Screens(route = "pomodoro_screen", icon = Icons.Default.AccessTime, title = "Pomodoro")

    object AvatarScreen :
        Screens(route = "avatar_screen", icon = Icons.Default.PersonOutline, title = "Avatar")

    object SettingsScreen :
        Screens(route = "settings_screen", icon = Icons.Default.Settings, title = "Settings")

    object PomodoroSettingsScreen : Screens(route = "pomodoro_settings_screen")
    object AccountSettingsScreen : Screens(route = "account_settings_screen")
    object NotificationsSettingsScreen : Screens(route = "notifications_settings_screen")

}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModelFactory: UserViewModelFactory,
    noteViewModelFactory: NoteViewModelFactory,
    moduleViewModelFactory: ModuleViewModelFactory
) {

    val userName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val loggedIn = remember { mutableStateOf<Boolean?>(false) }

    val pomodoroTimerViewModel = PomodoroTimerViewModel()
    val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)

    val noteViewModel: NoteViewModel = viewModel(factory = noteViewModelFactory)
    val noteState by noteViewModel.state.collectAsState()

    val moduleViewModel: ModuleViewModel = viewModel(factory = moduleViewModelFactory)
    val moduleState by moduleViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screens.LandingScreen.route
    ) {

        composable(Screens.LandingScreen.route) {
            LandingPage(navController = navController)
        }

        composable(Screens.SignUpScreen.route) {
            SignUpPage(
                viewModel = userViewModel,
                navController = navController,
                username = userName,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        }

        composable(Screens.LoginScreen.route) {
            LoginPage(
                viewModel = userViewModel,
                navController = navController,
                email = email,
                password = password,
                loggedIn = loggedIn
            )
        }

        composable(Screens.CameraScreen.route) {
            CameraPage(navController = navController)
        }

        composable(Screens.MicrophoneScreen.route) {
            val application = LocalContext.current.applicationContext as Application
            MicrophonePage(
                navController = navController,
                voiceToTextParser = VoiceToTextParser(application)
            )
        }

        composable(
            Screens.NotesScreen.route + "/{moduleId}",
            arguments = listOf(navArgument(name = "moduleId") { type = NavType.IntType })
        ) {
            NotesPage(
                navController = navController,
                state = noteState,
                onEvent = noteViewModel::onEvent
            )
        }

        composable(
            Screens.AddNoteScreen.route + "/{moduleId}",
            arguments = listOf(navArgument(name = "moduleId") { type = NavType.IntType }
            )
        ) {
            AddNotePage(
                navController = navController,
                state = noteState,
                onEvent = noteViewModel::onEvent
            )
        }

        composable(
            route = Screens.NoteScreen.route + "/{id}/{moduleId}",
            arguments = listOf(navArgument(name = "id") { type = NavType.IntType },
                navArgument("moduleId") { type = NavType.IntType })
        ) {
            NotePage(
                navController = navController,
                state = noteState,
                onEvent = noteViewModel::onEvent
            )
        }

        composable(Screens.ModulesScreen.route) {
            ModulesPage(
                navController = navController,
                state = moduleState,
                onEvent = moduleViewModel::onEvent
            )
        }

        composable(Screens.AddModuleScreen.route) {
            AddModulePage(
                navController = navController,
                state = moduleState,
                onEvent = moduleViewModel::onEvent
            )
        }


        composable(Screens.ChatScreen.route) {
            ChatPage(navController = navController)
        }

        composable(Screens.PomodoroScreen.route) {
            PomodoroPage(
                navController = navController,
                pomodoroTimerViewModel = pomodoroTimerViewModel
            )
        }

        composable(Screens.AvatarScreen.route) {
            AvatarPage(navController = navController)
        }

        composable(Screens.SettingsScreen.route) {
            SettingsPage(navController = navController)
        }

        composable(Screens.PomodoroSettingsScreen.route) {
            PomodoroSettingsPage(
                navController = navController,
                pomodoroTimerViewModel = pomodoroTimerViewModel
            )
        }

        composable(Screens.AccountSettingsScreen.route) {
            AccountSettingsPage(navController = navController, viewModel = userViewModel)
        }

        composable(Screens.NotificationsSettingsScreen.route) {
            NotificationsSettingsPage(navController = navController)
        }
    }
}
