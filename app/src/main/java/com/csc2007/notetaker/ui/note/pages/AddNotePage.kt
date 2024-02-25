import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import kotlinx.coroutines.launch

@Composable
fun AddNotePage(navController: NavHostController, state: NoteState, onEvent: (NoteEvent) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val moduleId = navController.currentBackStackEntry?.arguments?.getInt("moduleId") ?: -1
//    val id = navController.currentBackStackEntry?.arguments?.getInt("id") ?: -1

//    val note = state.notes.find { it.id == id }
//    val title = note?.title ?: ""
//    val content = note?.content ?: ""

//    state.title.value = title
//    state.content.value = content

//    Log.d("title in add note", title)
//
//    Log.d("ModuleId from AddNote", moduleId.toString())

    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (state.title.value.isEmpty() || state.content.value.isEmpty()) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please enter something")
                    }
                } else {
                    onEvent(
                        NoteEvent.SaveNote(
                            title = state.title.value,
                            content = state.content.value,
                            moduleId = moduleId,
                        )
                    )
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "Save notes")
            }
        },
        topBar = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures(onVerticalDrag = { _, dragAmount ->
                        if (dragAmount < 0) { // Swipe up detected
                            keyboardController?.hide()
                        }
                    })
                }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.title.value, onValueChange = {
                    state.title.value = it
                },
                textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
                placeholder = { Text(text = "Title") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                value = state.content.value, onValueChange = {
                    state.content.value = it
                },
                textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
                placeholder = { Text(text = "Content") }
            )
        }
    }
}

@Composable
fun CustomSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp), // Apply any desired modifiers
        snackbar = { snackbarData -> // Custom snackbar composable
            Snackbar(
                snackbarData = snackbarData,
                containerColor = Color.Blue, // Change the Snackbar background color here
                contentColor = Color.White, // Change the Snackbar text color here
                actionColor = Color.Yellow // Change the Snackbar action button color here
            )
        }
    )
}
