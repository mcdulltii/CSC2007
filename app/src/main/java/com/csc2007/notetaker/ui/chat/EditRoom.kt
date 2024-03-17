package com.csc2007.notetaker.ui.chat

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.ChatRoom
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.csc2007.notetaker.ui.TopNavBarText
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URI
import java.sql.Timestamp

@Composable
fun EditRoom(
    navController: NavHostController,
    viewModel: UserViewModel,
    firestore_db: FirebaseFirestore,
    roomName: MutableState<String>,
    roomId: String
) {
    val emailToAdd = rememberSaveable { mutableStateOf("") }
    val roomNameState = rememberSaveable { mutableStateOf(roomName.value) }
    val usersToAdd = rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    val username by viewModel.loggedInUserUsername.collectAsState()

    val roomObserver = ChatRoomViewModel(firestore_db = firestore_db)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopNavBarText(title = "Editing: ${roomName.value}", navBack = true)
        // Room name edit field
        Text("Edit Room Name:")

        TextField(
            value = roomNameState.value,
            onValueChange = { roomNameState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { Log.d("Room Creation", "User entered a name") }
            ),
            maxLines = 1
        )

        Spacer(modifier = Modifier.padding(16.dp))

        // People's Emails to add
        Text("People's Emails to add:")

        // Add email field
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
        ) {
            TextField(
                value = emailToAdd.value,
                onValueChange = { emailToAdd.value = it },
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            IconButton(onClick = {
                if (emailToAdd.value.isNotBlank() && emailToAdd.value !in usersToAdd.value) {
                    usersToAdd.value.add(emailToAdd.value)
                    Log.d("add email", "Added ${emailToAdd.value}")
                    emailToAdd.value = ""
                }
            }) {
                Icon(Icons.Filled.AddCircleOutline, contentDescription = "Add Email")
            }
        }

        // Added emails
        emailList(usersToAdd = usersToAdd.value, emailToAdd)


        // Action buttons
        displayActionButtons(
            navController = navController,
            roomNameState = roomNameState,
            roomName = roomName,
            roomObserver = roomObserver,
            username = username,
            usersToAdd = usersToAdd,
            roomId = roomId
        )
    }
}


@Composable
fun emailList(usersToAdd: MutableList<String>, emailToAdd: MutableState<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(usersToAdd) { email ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = email, modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    usersToAdd.remove(email)
                    // force a recomposition?
                    emailToAdd.value = " "
                    emailToAdd.value = ""
                    Log.d("tag", "Attempting to delete ${email}")
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Delete Email")
                }
            }
        }
    }
}

//fun removeEmail(usersToAdd: MutableList<String>, email: String) {
//    usersToAdd.remove(email)
//}


@Composable
fun displayActionButtons(navController: NavController,
                         roomNameState: MutableState<String>,
                         roomName: MutableState<String>,
                         roomObserver: ChatRoomViewModel,
                         username: String,
                         usersToAdd: MutableState<MutableList<String>>,
                         roomId: String
){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Cancel")
        }
        Button(onClick = {
            if (roomNameState.value.isNotEmpty()) {
                roomName.value = roomNameState.value
                val currentTimeMillis = System.currentTimeMillis()
                val currentTimeStamp = Timestamp(currentTimeMillis)
                roomObserver.updateRoom(username = username, newName = roomNameState.value, usersToAdd = usersToAdd.value, time_stamp = currentTimeStamp, roomId = roomId)
                navController.popBackStack()
            }
        }) {
            Text(text = "Confirm Update")
        }
    }
}
//@Composable
//fun EditRoomDialog(
//    onRoomEdited: (ChatRoom) -> Unit,
//    room_name: String,
//    room_id: String
//) {
//
//        Dialog(
//            onDismissRequest = { showDialog.value = false }
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Text(text = "Edit ${room_name}", fontWeight = FontWeight.Bold, color = Color.Yellow)
//
//                val emailToAdd = rememberSaveable { mutableStateOf("") }
//
//                val roomNameState = rememberSaveable { mutableStateOf("${room_name}") }
//
//                // For adding new users based on their email
//                val usersToAdd = rememberSaveable { mutableStateOf(mutableListOf<String>()) }
//
//                // Room name edit field
//                TextField(
//                    value = roomNameState.value,
//                    onValueChange = { roomNameState.value = it },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                        .clip(RoundedCornerShape(8.dp)),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        imeAction = ImeAction.Done
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onDone = { Log.d("Room Creation", "User entered a name") }
//                    ),
//                    maxLines = 1
//                )
//
//                // Add email field
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    TextField(
//                        value = emailToAdd.value,
//                        onValueChange = { emailToAdd.value = it },
//                        modifier = Modifier.weight(1f)
//                    )
//                    IconButton(onClick = {
//                        if (emailToAdd.value.isNotBlank() && emailToAdd.value !in usersToAdd.value) {
//                            usersToAdd.value.add(emailToAdd.value)
//                            emailToAdd.value = ""
//                        }
//                    }) {
//                        Icon(Icons.Filled.Add, contentDescription = "Add Email")
//                    }
//                }
//
//                // Display added emails with delete option
//                for (email in usersToAdd.value) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(text = email, modifier = Modifier.weight(1f))
//                        IconButton(onClick = { usersToAdd.value.remove(email) }) {
//                            Icon(Icons.Filled.Close, contentDescription = "Delete Email")
//                        }
//                    }
//                }
//
//
//            }
//        }
//    }
