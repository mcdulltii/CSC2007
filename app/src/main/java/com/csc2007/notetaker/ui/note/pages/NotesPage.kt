package com.csc2007.notetaker.ui.note.pages

import NoteAppBarWithSortTypes
import NoteSearchBar
import ExpandableFloatingActionButton
import NoteItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import com.csc2007.notetaker.ui.util.Screens

@Composable
fun NotesPage(
    navController: NavHostController,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }


    val moduleId = navController.currentBackStackEntry?.arguments?.getInt("moduleId") ?: -1


    Log.d("ModuleID from state", state.moduleId.value.toString())


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
                        moduleId = moduleId
                    )
                }
            }
        }
    }
}
