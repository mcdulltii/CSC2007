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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.AvatarViewModel
import com.csc2007.notetaker.database.viewmodel.AvatarViewModelFactory
import com.csc2007.notetaker.database.viewmodel.ItemViewModel
import com.csc2007.notetaker.database.viewmodel.ItemViewModelFactory
import com.csc2007.notetaker.database.viewmodel.OwnViewModel
import com.csc2007.notetaker.database.viewmodel.OwnViewModelFactory
import com.csc2007.notetaker.database.viewmodel.PomodoroTimerViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModel
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModelFactory
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModel
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModelFactory
import com.csc2007.notetaker.ui.LandingPage
import com.csc2007.notetaker.ui.avatar.AvatarEditPage
import com.csc2007.notetaker.ui.avatar.AvatarPage
import com.csc2007.notetaker.ui.avatar.AvatarShopPage
import com.csc2007.notetaker.ui.camera.CameraPage
import com.csc2007.notetaker.ui.chat.ChatPage
import com.csc2007.notetaker.ui.chat.EditRoom
import com.csc2007.notetaker.ui.chat.PrivateChatPage
import com.csc2007.notetaker.ui.login.LoginPage
import com.csc2007.notetaker.ui.microphone.MicrophonePage
import com.csc2007.notetaker.ui.microphone.VoiceToTextParser
import com.csc2007.notetaker.ui.module.pages.AddModulePage
import com.csc2007.notetaker.ui.module.pages.ModulesPage
import com.csc2007.notetaker.ui.note.pages.AddNotePage
import com.csc2007.notetaker.ui.note.pages.NotePage
import com.csc2007.notetaker.ui.note.pages.NotesPage
import com.csc2007.notetaker.ui.pomodoro.PomodoroPage
import com.csc2007.notetaker.ui.settings.AccountSettingsPage
import com.csc2007.notetaker.ui.settings.ChangePasswordPage
import com.csc2007.notetaker.ui.settings.NotificationsSettingsPage
import com.csc2007.notetaker.ui.settings.PomodoroSettingsPage
import com.csc2007.notetaker.ui.settings.SettingsPage
import com.csc2007.notetaker.ui.signup.SignUpPage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
        Screens(route = "modules_screen", icon = FontAwesomeIcons.Regular.StickyNote, title = "Modules")

    object ChatScreen :
        Screens(route = "chat_screen", icon = Icons.Default.ChatBubbleOutline, title = "Chat")

    object PrivateChatScreen :
        Screens(route = "private_chat_screen", icon = Icons.Default.ChatBubbleOutline, title = "Private Chat")

    object EditChatRoomScreen:
            Screens(route = "edit_chat_room_screen", icon = Icons.Default.ChatBubbleOutline, title = "Edit Room")

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
fun NavGraph(
    navController: NavHostController, 
    userViewModelFactory: UserViewModelFactory,
    noteViewModelFactory: NoteViewModelFactory,
    moduleViewModelFactory: ModuleViewModelFactory,
    itemViewModelFactory: ItemViewModelFactory, 
    ownViewModelFactory: OwnViewModelFactory, 
    avatarViewModelFactory: AvatarViewModelFactory,
    firestore_db : FirebaseFirestore,
    firestorage : FirebaseStorage
) {

    val pomodoroTimerViewModel = PomodoroTimerViewModel()
    val userViewModel : UserViewModel = viewModel(factory = userViewModelFactory)
    val itemViewModel: ItemViewModel = viewModel(factory = itemViewModelFactory)
    val ownViewModel: OwnViewModel = viewModel(factory = ownViewModelFactory)
    val avatarViewModel: AvatarViewModel = viewModel(factory = avatarViewModelFactory)
    
    val noteViewModel: NoteViewModel = viewModel(factory = noteViewModelFactory)
    val noteState by noteViewModel.state.collectAsState()

    val moduleViewModel: ModuleViewModel = viewModel(factory = moduleViewModelFactory)
    val moduleState by moduleViewModel.state.collectAsState()


    val selectedRoomID = rememberSaveable{ mutableStateOf("")}
    val selectedRoomName = rememberSaveable{ mutableStateOf("")}

    NavHost(
        navController = navController,
        startDestination = Screens.LandingScreen.route
    ) {

        composable(Screens.LandingScreen.route) {
            LandingPage(navController = navController)
        }

        composable(Screens.SignUpScreen.route) {
            SignUpPage(
              userViewModel = userViewModel, 
              avatarViewModel = avatarViewModel, 
              navController = navController)
        }

        composable(Screens.LoginScreen.route) {
            LoginPage(
                userViewModel = userViewModel,
                navController = navController,
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
            ChatPage(navController = navController, viewModel = userViewModel, firestore_db = firestore_db, selectedRoomID = selectedRoomID, selectedRoomName = selectedRoomName)
        }

        composable(Screens.PrivateChatScreen.route) {
            val user by userViewModel.loggedInUser.collectAsState()
            val userId = user?.id
            PrivateChatPage(navController = navController, viewModel = userViewModel, firestore_db = firestore_db, roomName = selectedRoomName.value, roomId = selectedRoomID.value)
        }                                                                                                          /* TODO change to selectedRoom.value */

        composable(Screens.EditChatRoomScreen.route)
        {
            EditRoom(navController = navController, viewModel = userViewModel, firestore_db = firestore_db, roomName = selectedRoomName, roomId = selectedRoomID.value)
        }


        composable(Screens.PomodoroScreen.route) {
            PomodoroPage(
                navController = navController,
                pomodoroTimerViewModel = pomodoroTimerViewModel
            )
        }

        composable(Screens.AvatarScreen.route) {
            AvatarPage(navController = navController, userViewModel = userViewModel, avatarViewModel = avatarViewModel)
        }

        composable(Screens.AvatarEditScreen.route) {
            AvatarEditPage(navController = navController, userViewModel = userViewModel, itemViewModel = itemViewModel, ownViewModel = ownViewModel, avatarViewModel = avatarViewModel)
        }

        composable(Screens.AvatarShopScreen.route) {
            AvatarShopPage(navController = navController, userViewModel = userViewModel, itemViewModel = itemViewModel, ownViewModel = ownViewModel)
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

        composable(Screens.ChangePasswordScreen.route) {
            ChangePasswordPage(navController = navController, viewModel = userViewModel)
        }

        composable(Screens.NotificationsSettingsScreen.route) {
            NotificationsSettingsPage(navController = navController)
        }
    }
}
