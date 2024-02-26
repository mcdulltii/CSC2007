package com.csc2007.notetaker.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.database.viewmodel.PomodoroTimerViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.ui.LandingPage
import com.csc2007.notetaker.ui.avatar.AvatarEditPage
import com.csc2007.notetaker.ui.avatar.AvatarPage
import com.csc2007.notetaker.ui.avatar.AvatarShopPage
import com.csc2007.notetaker.ui.camera.CameraPage
import com.csc2007.notetaker.ui.chat.ChatPage
import com.csc2007.notetaker.ui.individual_note.IndividualNotePage
import com.csc2007.notetaker.ui.login.LoginPage
import com.csc2007.notetaker.ui.modules.ModulesPage
import com.csc2007.notetaker.ui.note.NotesPage
import com.csc2007.notetaker.ui.pomodoro.PomodoroPage
import com.csc2007.notetaker.ui.settings.PomodoroSettingsPage
import com.csc2007.notetaker.ui.settings.AccountSettingsPage
import com.csc2007.notetaker.ui.settings.ChangePasswordPage
import com.csc2007.notetaker.ui.settings.NotificationsSettingsPage
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

    object LandingScreen: Screens(route = "landing_screen")
    object SignUpScreen: Screens(route = "signup_screen")
    object LoginScreen: Screens(route = "login_screen")
    object CameraScreen:
        Screens(route = "camera_screen", icon = FontAwesomeIcons.Solid.Camera, title = "Camera")

    object NotesScreen :
        Screens(route = "notes_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "Notes")

    object ModulesScreen :
        Screens(route = "modules_screen", icon = Icons.Default.MenuBook, title = "Modules")

    object IndividualNoteScreen :
        Screens(route = "individual_note_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "IndividualNote")

    object ChatScreen :
        Screens(route = "chat_screen", icon = Icons.Default.ChatBubbleOutline, title = "Chat")

    object PomodoroScreen :
        Screens(route = "pomodoro_screen", icon = Icons.Default.AccessTime, title = "Pomodoro")

    object AvatarScreen :
        Screens(route = "avatar_screen", icon = Icons.Default.PersonOutline, title = "Avatar")

    object SettingsScreen :
        Screens(route = "settings_screen", icon = Icons.Default.Settings, title = "Settings")

    object PomodoroSettingsScreen: Screens(route = "pomodoro_settings_screen")
    object AccountSettingsScreen: Screens(route = "account_settings_screen")
    object ChangePasswordScreen: Screens(route = "change_password_screen")
    object NotificationsSettingsScreen: Screens(route = "notifications_settings_screen")

    object AvatarShopScreen: Screens(route = "avatar_shop_screen")

    object AvatarEditScreen: Screens(route = "avatar_edit_screen")

}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun NavGraph(navController: NavHostController, viewModelFactory: UserViewModelFactory) {

    val pomodoroTimerViewModel = PomodoroTimerViewModel()
    val userViewModel : UserViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Screens.LandingScreen.route
    ) {

        composable(Screens.LandingScreen.route) {
            LandingPage(navController = navController)
        }

        composable(Screens.SignUpScreen.route) {
            SignUpPage(viewModel = userViewModel, navController = navController)
        }

        composable(Screens.LoginScreen.route) {
            LoginPage(viewModel = userViewModel, navController = navController)
        }

        composable(Screens.CameraScreen.route) {
            CameraPage(navController = navController)
        }
        
        composable(Screens.NotesScreen.route) {
            NotesPage(navController = navController)
        }

        composable(Screens.ModulesScreen.route) {
            ModulesPage(navController = navController)
        }
        
        composable(Screens.IndividualNoteScreen.route) {
            IndividualNotePage(navController = navController)
        }

        composable(Screens.ChatScreen.route) {
            ChatPage(navController = navController)
        }

        composable(Screens.PomodoroScreen.route) {
            PomodoroPage(navController = navController, pomodoroTimerViewModel = pomodoroTimerViewModel)
        }

        composable(Screens.AvatarScreen.route) {
            AvatarPage(navController = navController)
        }

        composable(Screens.AvatarEditScreen.route) {
            AvatarEditPage(navController = navController)
        }

        composable(Screens.AvatarShopScreen.route) {
            AvatarShopPage(navController = navController)
        }

        composable(Screens.SettingsScreen.route) {
            SettingsPage(navController = navController)
        }

        composable(Screens.PomodoroSettingsScreen.route) {
            PomodoroSettingsPage(navController = navController, pomodoroTimerViewModel = pomodoroTimerViewModel)
        }

        composable(Screens.AccountSettingsScreen.route) {
            AccountSettingsPage(navController = navController, viewModel = userViewModel)
        }

        composable(Screens.ChangePasswordScreen.route) {
            ChangePasswordPage(navController = navController, viewModel = userViewModel)
        }

        composable(Screens.NotificationsSettingsScreen.route) {
            NotificationsSettingsPage(navController = navController)
        }
    }
}
