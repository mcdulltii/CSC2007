package com.csc2007.notetaker.ui.note.pages

import ExpandableFloatingActionButton
import NoteAppBarWithSortTypes
import NoteItem
import NoteSearchBar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.ChatRoom
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import com.csc2007.notetaker.ui.util.Screens
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun NotesPage(
    navController: NavHostController,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    selectedRoomID: MutableState<String>,
    selectedRoomName: MutableState<String>,
    firestoreDb: FirebaseFirestore,
    userViewModel: UserViewModel,
    firestorage: FirebaseStorage
) {
    var isExpanded by remember { mutableStateOf(false) }

    val moduleId = navController.currentBackStackEntry?.arguments?.getInt("moduleId") ?: -1

    Log.d("ModuleID from state", state.moduleId.value.toString())
    val userRooms = rememberSaveable{mutableStateOf(emptyList<ChatRoom>())}
    val username by userViewModel.loggedInUserUsername.collectAsState()
    val email by userViewModel.loggedInUserEmail.collectAsState()

    val roomObserver = ChatRoomViewModel(firestore_db = firestoreDb, username = username, email = email)

    // Attach snapshot listener
    LaunchedEffect(Unit) {
        roomObserver.getAllRooms(roomsUserIsIn = userRooms, userEmail = email)
    }

    val notes = state.notes.filter { note ->
        note.moduleId == moduleId
    }

    Scaffold(
        floatingActionButton = {
            ExpandableFloatingActionButton(
                isExpanded = isExpanded,
                onExpand = { isExpanded = !isExpanded },
                onClickToAddManually = {
                    state.title.value = ""
                    state.content.value = ""
                    navController.navigate(Screens.AddNoteScreen.route + "/${moduleId}")
                },
                onClickToCamera = {
                    navController.navigate(Screens.CameraScreen.route + "/${moduleId}")
                },
                onClickToAddAudio = {
                    navController.navigate(Screens.MicrophoneScreen.route + "/${moduleId}")
                }
            )
        },
        topBar = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    NoteSearchBar(state = state, onEvent = onEvent)
                }
                NoteAppBarWithSortTypes(onEvent = onEvent)
            }
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notes.size) { index ->
                    NoteItem(
                        state = state,
                        index = index,
                        onEvent = onEvent,
                        navController = navController,
                        notes = notes,
                        moduleId = moduleId,
                        roomObserver = roomObserver,
                        selectedRoomID = selectedRoomID,
                        selectedName = selectedRoomName,
                        userRooms = userRooms.value,
                        firestorage = firestorage,
                    )
                }
            }
        }
    }
}
