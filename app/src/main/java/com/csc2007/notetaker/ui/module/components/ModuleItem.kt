package com.csc2007.notetaker.ui.module.components


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.csc2007.notetaker.database.viewmodel.module.ModuleEvent
import com.csc2007.notetaker.database.viewmodel.module.ModuleState
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.ui.note.util.formatDate
import com.csc2007.notetaker.ui.util.Screens


@Composable
fun CircularIconWithLetter(letter: Char) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp) // Set the size of the circular icon
            .background(
                Color(0xFF30628C),
                CircleShape
            ) // Set the background to blue and shape to circular
    ) {
        Text(
            text = letter.toString(), // The letter to be displayed
            color = Color.White, // Set the text color to white for contrast
            fontSize = 24.sp // Set the font size
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModuleItem(
    state: ModuleState,
    index: Int,
    onEvent: (ModuleEvent) -> Unit,
    navController: NavController
) {

    val context = LocalContext.current // Get the current context
    val filesDir = context.filesDir // Access filesDir from the context

    // Ensure title is not empty before accessing first character; provide default if empty
    val title = state.modules[index].title
    val firstChar = if (title.isNotEmpty()) title.first() else 'N' // 'N' as a default placeholder

    val imageName = state.modules[index].imagePath
    val imageUri =
        if (imageName.isNotEmpty()) "file://$filesDir/$imageName" else null // Ensure to handle empty paths correctly

    val moduleId = state.modules[index].id
    val formattedDateAdded = formatDate(state.modules[index].dateCreated)

    var showDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFF2F3F9), shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = {
                    navController.navigate(Screens.NotesScreen.route + "/" + moduleId)
                },
                onLongClick = {
                    showDialog = !showDialog
                }
            )

        ,
        contentAlignment = Alignment.TopStart
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        top = 10.dp,
                        bottom = 10.dp

                    )
                    .fillMaxSize()
                    , verticalAlignment = Alignment.CenterVertically


            ) { // Reduced overall padding, adjust as needed
                CircularIconWithLetter(firstChar)
                Spacer(Modifier.width(8.dp)) // Reduced spacer width to decrease left padding
                Column() {
                    Text(
                        text = title,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.width(210.dp) // Keep or adjust the specific width as needed
                    )
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Date Added: $formattedDateAdded",
                        style = TextStyle(fontSize = 14.sp, color = Color(0xFF757575))
                    )
                }
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Module Image",
                    contentScale = ContentScale.Crop, // Adjusted for a better fill
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp) // Increased width to match the height for a square aspect
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onEvent(ModuleEvent.DeleteModule(module = state.modules[index]))
                    }
                ) {
                    Text("DELETE", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}
