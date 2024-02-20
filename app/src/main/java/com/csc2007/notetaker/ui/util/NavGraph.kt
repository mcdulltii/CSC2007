package com.csc2007.notetaker.ui.util

import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.ui.LandingPage
import com.csc2007.notetaker.ui.avatar.AvatarPage
import com.csc2007.notetaker.ui.camera.CameraPage
import com.csc2007.notetaker.ui.chat.ChatPage
import com.csc2007.notetaker.ui.chat.Chatter
import com.csc2007.notetaker.ui.chat.PrivateChatPage
import com.csc2007.notetaker.ui.individual_note.IndividualNotePage
import com.csc2007.notetaker.ui.login.LoginPage
import com.csc2007.notetaker.ui.modules.ModulesPage
import com.csc2007.notetaker.ui.settings.SettingsPage
import com.csc2007.notetaker.ui.signup.SignUpPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.StickyNote
import kotlinx.coroutines.ExperimentalCoroutinesApi


sealed class Screens(val route: String, val title: String? = null, val icon: ImageVector? = null) {

    object LandingScreen: Screens(route = "landing_screen")
    object SignUpScreen: Screens(route = "signup_screen")
    object LoginScreen: Screens(route = "login_screen")
    object CameraScreen: Screens(route = "camera_screen")

    object NotesScreen :
        Screens(route = "notes_screen", icon = Icons.Default.MenuBook, title = "Notes")

    object ModulesScreen :
        Screens(route = "modules_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "Modules")

    object IndividualNoteScreen :
        Screens(route = "individual_note_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "IndividualNote")

    object ChatScreen :
        Screens(route = "chat_screen", icon = Icons.Default.ChatBubbleOutline, title = "Chat")

    object PomodoroScreen :
        Screens(route = "pomodoro_screen", icon = Icons.Default.AccessTime, title = "Pomodoro")

    object AvatarScreen :
        Screens(route = "avatar_screen", icon = Icons.Default.PersonOutline, title = "Avatar")

    object SettingsScreen :
        Screens(route = "avatar_screen", icon = Icons.Default.Settings, title = "Settings")

}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun NavGraph(navController: NavHostController, viewModelFactory: UserViewModelFactory) {

    val userId = remember{ mutableStateOf(-1)}
    val userName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val loggedIn = remember { mutableStateOf<Boolean?>(false) }
    val privateChat = remember{ mutableStateOf<Chatter>(
        Chatter(id = 999,
        userName = "Kacie",
        lastSentTo = "Sandra Adams",
        latestText = " - It’s the one week of the year in which you get the chance to take…",
        imgDrawable = R.drawable.kacie),)}
    NavHost(
        navController = navController,
        startDestination = Screens.LandingScreen.route
    ) {

        composable(Screens.LandingScreen.route) {
            LandingPage(navController = navController)
        }

        composable(Screens.LoginScreen.route) {
            val viewModel : UserViewModel = viewModel(factory = viewModelFactory)
            LoginPage(viewModel = viewModel, navController = navController, email = email, password = password, loggedIn = loggedIn, userId = userId)
        }

        composable(Screens.SignUpScreen.route) {
            val viewModel : UserViewModel = viewModel(factory = viewModelFactory)
            SignUpPage(viewModel = viewModel, navController = navController, username = userName, email = email, password = password, confirmPassword = confirmPassword)
        }

        composable(Screens.CameraScreen.route) {
            CameraPage(navController = navController)
        }


        composable(Screens.ModulesScreen.route) {
            ModulesPage(navController = navController)
        }

        composable(Screens.ChatScreen.route) {
            val viewModel : UserViewModel = viewModel(factory = viewModelFactory)
            ChatPage(navController = navController, viewModel = viewModel, select_chat = privateChat)
        }

        composable("private_chat_screen")
        {
            val viewModel : UserViewModel = viewModel(factory = viewModelFactory)
            PrivateChatPage(navController = navController, viewModel = viewModel, selected_chatter = privateChat.value, userId = userId.value)
        }

        composable(Screens.PomodoroScreen.route) {
            IndividualNotePage(navController = navController)
        }
        composable(Screens.AvatarScreen.route) {
            AvatarPage(navController = navController)
        }
        composable(Screens.SettingsScreen.route) {
            SettingsPage(navController = navController)
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Screens.ModulesScreen, Screens.ChatScreen, Screens.PomodoroScreen, Screens.AvatarScreen, Screens.SettingsScreen
    )

    NavigationBar(
        Modifier.requiredHeight(90.dp),
        containerColor =  MaterialTheme.colorScheme.primary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(
                label = {
                    Text(
                        text = screen.title!!,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                icon = {
                    screen.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp) // Adjust the size of the icon here
                        )
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Black, selectedTextColor = Color.Gray
                ),
            )
        }
    }
}
