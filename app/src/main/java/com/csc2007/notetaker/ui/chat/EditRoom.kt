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
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp

@Composable
fun EditRoom(
    navController: NavHostController,
    viewModel: UserViewModel,
    firestoreDb: FirebaseFirestore,
    roomName: MutableState<String>,
    roomId: String
) {
    val emailToAdd = rememberSaveable { mutableStateOf("") }
    val roomNameState = rememberSaveable { mutableStateOf(roomName.value) }
    val usersToAdd = rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    val username by viewModel.loggedInUserUsername.collectAsState()

    val roomObserver = ChatRoomViewModel(firestore_db = firestoreDb)

    Column(modifier = Modifier.fillMaxSize())
    {

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
            {
                Text("Editing: ${roomName.value}", maxLines = 1, modifier = Modifier.padding(16.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
            // Room name edit field
            Row(horizontalArrangement = Arrangement.Center)
            {
                Text("Edit Room\n Name:", modifier = Modifier.padding(16.dp, 8.dp))
                TextField(
                    value = roomNameState.value,
                    onValueChange = { roomNameState.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { Log.d("Room Creation", "User entered a name") }
                    ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            // People's Emails to add
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text("Invite people \nvia Email:", modifier = Modifier.padding(16.dp, 8.dp))
                Column(modifier = Modifier.padding(8.dp)) {
                    // Add email field
                    Row() {
                        TextField(
                            value = emailToAdd.value,
                            onValueChange = { emailToAdd.value = it },
                            modifier = Modifier.weight(1f)
                                .fillMaxWidth()
                                .padding()
                                .clip(RoundedCornerShape(8.dp)),
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
                }
            }
            EmailList(usersToAdd = usersToAdd.value, emailToAdd)

        }

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(4.dp))
        {
            DisplayActionButtons(
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
}



@Composable
fun EmailList(usersToAdd: MutableList<String>, emailToAdd: MutableState<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(usersToAdd) { email ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = email, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                IconButton(onClick = {
                    usersToAdd.remove(email)
                    // force a recomposition?
                    emailToAdd.value = " "
                    emailToAdd.value = ""
                    Log.d("tag", "Attempting to delete $email")
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Delete Email")
                }
            }
        }
    }
}

@Composable
fun DisplayActionButtons(navController: NavController,
                         roomNameState: MutableState<String>,
                         roomName: MutableState<String>,
                         roomObserver: ChatRoomViewModel,
                         username: String,
                         usersToAdd: MutableState<MutableList<String>>,
                         roomId: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
            onClick = {
            roomObserver.deleteRoom(roomId)
            navController.popBackStack()
            navController.popBackStack() },
            modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Delete")
        }
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Cancel")
        }
        Button(enabled = roomNameState.value.isNotEmpty(),
            onClick = {
            if (roomNameState.value.isNotEmpty()) {
                roomName.value = roomNameState.value
                val currentTimeMillis = System.currentTimeMillis()
                val currentTimeStamp = Timestamp(currentTimeMillis)
                roomObserver.updateRoom(username = username, newName = roomNameState.value, usersToAdd = usersToAdd.value, time_stamp = currentTimeStamp, roomId = roomId)
                navController.popBackStack()
            }
        }) {
            Text(text = "Update")
        }
    }
}
