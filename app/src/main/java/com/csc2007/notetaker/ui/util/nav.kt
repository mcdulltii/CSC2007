package com.csc2007.notetaker.ui.util

import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.csc2007.notetaker.ui.avatar.AvatarPage
import com.csc2007.notetaker.ui.individual_note.IndividualNotePage
import com.csc2007.notetaker.ui.modules.ModulesPage
import com.csc2007.notetaker.ui.note.NotesPage
import com.csc2007.notetaker.ui.settings.SettingsPage
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.CommentDots
import compose.icons.fontawesomeicons.regular.StickyNote
import compose.icons.fontawesomeicons.regular.TimesCircle
import compose.icons.fontawesomeicons.regular.User


sealed class Screens(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object NotesScreen :
        Screens(route = "notes_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "Notes")

    object ModulesScreen :
        Screens(route = "modules_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "Modules")

    object IndividualNoteScreen :
        Screens(route = "individual_note_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "IndividualNote")

    object ChatScreen :
        Screens(route = "chat_screen", icon = FontAwesomeIcons.Regular.CommentDots, title = "Chat")

    object PomodoroScreen :
        Screens(route = "pomodoro_screen", icon = FontAwesomeIcons.Regular.TimesCircle, title = "Pomodoro")

    object AvatarScreen :
        Screens(route = "avatar_screen", icon = FontAwesomeIcons.Regular.User, title = "Avatar")

    object SettingsScreen :
        Screens(route = "avatar_screen", icon = Icons.Default.Settings, title = "Settings")

}


@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.ModulesScreen.route) {
        composable(Screens.ModulesScreen.route) {
            ModulesPage(navController = navController)
        }
        composable(Screens.ChatScreen.route) {
            NotesPage(navController = navController)
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
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Screens.ModulesScreen, Screens.ChatScreen, Screens.PomodoroScreen, Screens.AvatarScreen, Screens.SettingsScreen
    )

    NavigationBar(
        Modifier.requiredHeight(90.dp),
        containerColor =  androidx.compose.material.MaterialTheme.colors.primary,
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
