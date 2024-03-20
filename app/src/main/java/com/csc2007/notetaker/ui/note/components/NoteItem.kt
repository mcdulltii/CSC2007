import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.csc2007.notetaker.database.ChatRoom
import com.csc2007.notetaker.database.Note
import com.csc2007.notetaker.database.repository.ChatRoomFileCollection
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import com.csc2007.notetaker.ui.note.util.formatDate
import com.csc2007.notetaker.ui.note.util.generatePDF
import com.csc2007.notetaker.ui.util.Screens
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp

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

@Composable
fun NoteItem(
    state: NoteState,
    index: Int,
    onEvent: (NoteEvent) -> Unit,
    navController: NavController,
    notes: List<Note>,
    moduleId: Int,
    roomObserver: ChatRoomViewModel,
    selectedRoomID: MutableState<String>,
    selectedName: MutableState<String>,
    userRooms: List<ChatRoom>,
    firestorage: FirebaseStorage,
    username: String
) {

    val firstChar = if (notes[index].title.isNotEmpty()) notes[index].title.first() else 'N'
    val title = notes[index].title
    val content = notes[index].content
    val id = notes[index].id

    val context = LocalContext.current



    val formattedDateAdded = formatDate(notes[index].dateAdded)

    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // State to control visibility of confirmation dialog
    var showShareModal by remember { mutableStateOf(false) } // State to control visibility of share modal



    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFF2F3F9), shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
            .clickable { navController.navigate(Screens.NoteScreen.route + "/" + id + "/" + moduleId) },
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            Row(
                modifier = Modifier.padding(
                    start = 10.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
            ) { // Reduced overall padding, adjust as needed
                CircularIconWithLetter(firstChar)
                Spacer(Modifier.width(8.dp)) // Reduced spacer width to decrease left padding
                Column {
                    Text(
                        text = title,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.width(250.dp) // Keep or adjust the specific width as needed
                    )
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Date Added: $formattedDateAdded",
                        style = TextStyle(fontSize = 14.sp, color = Color(0xFF757575))
                    )
                }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Black
                    )
                }
            }
            Spacer(Modifier.height(5.dp))

            Text(
                text = content,
                modifier = Modifier.padding(10.dp)
            ) // Add padding if content text needs it
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Black
                    )
                },
                onClick = {
                    // Handle "Delete" click
                    showMenu = false
                    showDialog = true // Show confirmation dialog
                }
            )
            DropdownMenuItem(
                text = { Text("Share") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color.Black
                    )
                },
                onClick = {
                    // Handle "Share" click
                    showMenu = false
                    showShareModal = true
                }
            )
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
                            onEvent(NoteEvent.DeleteNote(note = notes[index]))
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

        if (showShareModal) {


            AlertDialog(
                onDismissRequest = { showShareModal = false },
                title = { Text("Choose the chat") },
                text = {
                    LazyColumn {
                        items(userRooms) { userRoom ->
                            userRoom.room_name?.let {

                                Text(it, modifier = Modifier.clickable {

                                    selectedRoomID.value = userRoom.roomId!!
                                    selectedName.value = userRoom.room_name

                                    val fileCollection = ChatRoomFileCollection(firestorage = firestorage, roomObserver = roomObserver, time_stamp = Timestamp(System.currentTimeMillis()), username = username)
                                    val pdfFile = generatePDF(context = context, content = content)
                                    fileCollection.addFile(selectedRoomID.value, fileName = title, fileByteArr = pdfFile)
                                    navController.navigate(Screens.PrivateChatScreen.route)
                                })
                            }


                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showShareModal = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
