package com.csc2007.notetaker.ui.util

import AddNotePage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
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
import com.csc2007.notetaker.ui.module.pages.AddModulePage
import com.csc2007.notetaker.ui.module.pages.ModulesPage
import com.csc2007.notetaker.ui.note.pages.NotePage
import com.csc2007.notetaker.ui.note.pages.NotesPage
import com.csc2007.notetaker.ui.pomodoro.PomodoroPage
import com.csc2007.notetaker.ui.settings.SettingsPage
import com.csc2007.notetaker.ui.signup.SignUpPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Clock
import compose.icons.fontawesomeicons.regular.CommentDots
import compose.icons.fontawesomeicons.regular.StickyNote
import compose.icons.fontawesomeicons.regular.User
import compose.icons.fontawesomeicons.solid.Camera
import kotlinx.coroutines.ExperimentalCoroutinesApi


sealed class Screens(val route: String, val title: String? = null, val icon: ImageVector? = null) {

    object LandingScreen : Screens(route = "landing_screen")
    object SignUpScreen : Screens(route = "signup_screen")
    object LoginScreen : Screens(route = "login_screen")
    object CameraScreen :
        Screens(route = "camera_screen", icon = FontAwesomeIcons.Solid.Camera, title = "Camera")

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
        Screens(route = "chat_screen", icon = FontAwesomeIcons.Regular.CommentDots, title = "Chat")

    object PomodoroScreen :
        Screens(
            route = "pomodoro_screen",
            icon = FontAwesomeIcons.Regular.Clock,
            title = "Pomodoro"
        )

    object AvatarScreen :
        Screens(route = "avatar_screen", icon = FontAwesomeIcons.Regular.User, title = "Avatar")

    object SettingsScreen :
        Screens(route = "settings_screen", icon = Icons.Default.Settings, title = "Settings")

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


    val noteViewModel: NoteViewModel = viewModel(factory = noteViewModelFactory)
    val noteState by noteViewModel.state.collectAsState()

    val moduleViewModel: ModuleViewModel = viewModel(factory = moduleViewModelFactory)
    val moduleState by moduleViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screens.ModulesScreen.route
    ) {

        composable(Screens.LandingScreen.route) {
            LandingPage(navController = navController)
        }

        composable(Screens.SignUpScreen.route) {
            val viewModel: UserViewModel = viewModel(factory = viewModelFactory)
            SignUpPage(
                viewModel = viewModel,
                navController = navController,
                username = userName,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        }

        composable(Screens.LoginScreen.route) {
            val viewModel: UserViewModel = viewModel(factory = viewModelFactory)
            LoginPage(
                viewModel = viewModel,
                navController = navController,
                email = email,
                password = password,
                loggedIn = loggedIn
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



        composable(Screens.CameraScreen.route) {
            CameraPage(navController = navController)
        }


        composable(Screens.ChatScreen.route) {
            ChatPage(navController = navController)
        }

        composable(Screens.PomodoroScreen.route) {
            PomodoroPage(navController = navController)
        }

        composable(Screens.AvatarScreen.route) {
            AvatarPage(navController = navController)
        }

        composable(Screens.SettingsScreen.route) {
            SettingsPage(navController = navController)
        }
    }
}
