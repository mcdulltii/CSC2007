package com.csc2007.notetaker.ui.note.pages


import NoteAppBarWithBackButton
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.ml.quaterion.text2summary.Text2Summary


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotePage(
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

    val id = navController.currentBackStackEntry?.arguments?.getInt("id") ?: -1

    val note = state.notes.find { it.id == id }
    val title = note?.title
    val content = note?.content

    if (title != null) {
        state.title.value = title
    }

    if (content != null) {
        state.content.value = content
    }


    if (note != null) {
        state.moduleId.value = note.moduleId
    }

    if (note != null) {
        state.noteId.value= note.id
    }

    val userRooms = rememberSaveable { mutableStateOf(emptyList<ChatRoom>()) }
    val username by userViewModel.loggedInUserUsername.collectAsState()
    val email by userViewModel.loggedInUserEmail.collectAsState()

    val roomObserver =
        ChatRoomViewModel(firestore_db = firestoreDb, username = username, email = email)

    // Attach snapshot listener
    LaunchedEffect(Unit) {
        roomObserver.getAllRooms(roomsUserIsIn = userRooms, userEmail = email)
    }

    Scaffold(
        topBar = {
            if (title != null) {
                NoteAppBarWithBackButton(
                    navController = navController,
                    title = title,
                    onClickDelete = {
                        onEvent(NoteEvent.DeleteNote(note = note))
                        navController.popBackStack()
                    },
                    onClickSummary = {
                        if (content != null) {
                            val callback = object : Text2Summary.SummaryCallback {
                                override fun onSummaryProduced(summary: String) {
                                    onEvent(NoteEvent.UpdateNote(note.copy(content = summary)))
                                }
                            }
                            Text2Summary.summarizeAsync(content, 0.7f, callback)
                        }
                    },
                    onClickEdit = { navController.navigate(Screens.EditNoteScreen.route) },
                    shareContent = content,
                    roomObserver = roomObserver,
                    selectedRoomID = selectedRoomID,
                    selectedName = selectedRoomName,
                    userRooms = userRooms.value,
                    firestorage = firestorage,
                )
            }
        },

        ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (content != null) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}
