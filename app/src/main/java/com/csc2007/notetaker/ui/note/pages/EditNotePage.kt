package com.csc2007.notetaker.ui.note.pages


import NoteAppBarWithBackButton
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.Note
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import com.csc2007.notetaker.ui.util.Screens
import com.google.type.DateTime


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditNotePage(
    navController: NavHostController, state: NoteState, onEvent: (NoteEvent) -> Unit
) {

    var editTitle: MutableState<String> = mutableStateOf(state.title.value)
    var editContent: MutableState<String> = mutableStateOf(state.content.value)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Edit Note") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, actions = {
                // Back button

                IconButton(onClick = {

                    Log.d("ID", state.noteId.value.toString())
                    Log.d("moduleID", state.moduleId.value.toString())


                    val note = Note(
                        id = state.noteId.value, title = editTitle.value, content = editContent.value,
                        moduleId = state.moduleId.value, dateAdded = System.currentTimeMillis()
                    )

                    onEvent(
                        NoteEvent.UpdateNote(
                            note = note
                        )
                    )
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = "Save"
                    )
                }
            })
        }
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
                TextField(
                    value = editTitle.value,
                    onValueChange = { editTitle.value = it },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    label = { Text("Title") } // Optional label
                )

                TextField(
                    value = editContent.value,
                    onValueChange = { editContent.value = it },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = { Text("Content") } // Optional label
                )
            }
        }
    }
}

