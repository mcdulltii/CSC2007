package com.csc2007.notetaker.ui.note.pages


import NoteAppBarWithBackButton
import ExpandableFloatingActionButton
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import com.csc2007.notetaker.ui.util.Screens


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotePage(navController: NavHostController, state: NoteState, onEvent: (NoteEvent) -> Unit) {


    var isExpanded by remember { mutableStateOf(false) }

    val id = navController.currentBackStackEntry?.arguments?.getInt("id") ?: -1
    val moduleId = navController.currentBackStackEntry?.arguments?.getInt("moduleId") ?: -1

    val note = state.notes.find { it.id == id }
    val title = note?.title
    val content = note?.content


    Scaffold(
        topBar = {
            if (title != null) {
                NoteAppBarWithBackButton(navController = navController, title = title)
            }
        },
        floatingActionButton = {
            ExpandableFloatingActionButton(

                isExpanded = isExpanded,
                onExpand = { isExpanded = !isExpanded },
                onClickToAddManually = {
                    navController.navigate(Screens.AddNoteScreen.route + "/$moduleId/$id")
                }
                )

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
