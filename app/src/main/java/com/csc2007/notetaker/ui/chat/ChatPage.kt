package com.csc2007.notetaker.ui.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.ChatMessage
import com.csc2007.notetaker.database.ChatRoom
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatMessageViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.TopSearchBar
import com.csc2007.notetaker.ui.util.Screens
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import java.net.URI
import java.sql.Timestamp

@Composable
fun ChatPage(navController: NavHostController,
             firestore_db: FirebaseFirestore,
             viewModel: UserViewModel = viewModel(),
             selectedRoomID: MutableState<String>,
             selectedRoomName: MutableState<String>)
{
    val searchQuery = rememberSaveable{mutableStateOf("")}
    val searchIsActive = rememberSaveable{ mutableStateOf(false )}

    val username by viewModel.loggedInUserUsername.collectAsState()
    val email by viewModel.loggedInUserEmail.collectAsState()

    val roomObserver = ChatRoomViewModel(firestore_db = firestore_db, username = username, email = email) // TODO change this to the actual room ID

    val thisUsersRooms = rememberSaveable{mutableStateOf(emptyList<ChatRoom>())}
    val showDialog = rememberSaveable{mutableStateOf(false)}

    // Attach snapshot listener
    LaunchedEffect(Unit) {
        roomObserver.getAllRooms(roomsUserIsIn = thisUsersRooms, userEmail = email)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content inside the Box
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            content = {
                Column(
                    modifier = Modifier.weight(1.0f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        Spacer(modifier = Modifier.padding(6.dp))
                        TopSearchBar(search = searchQuery, isActive = searchIsActive)
                        Spacer(modifier = Modifier.padding(6.dp))

                        ChatList(
                            rooms = thisUsersRooms.value,
                            navController = navController,
                            select_room = selectedRoomID,
                            room_name = selectedRoomName,
                            query = searchQuery
                        )
                    }
                )
            }
        )

        // Overlapping CreateRoomButton at the bottom right
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(end = 16.dp, bottom = 80.dp).fillMaxSize()
        )
        {
            CreateRoomButton(showDialog = showDialog , email = email, roomObserver = roomObserver)
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom)
        {
            BottomNavBar(navController = navController)
        }
    }


}

@Composable
fun chatRow(room: ChatRoom, navController: NavHostController, select_room: MutableState<String>, room_name: MutableState<String>)
            {
                Row(modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        select_room.value = room.roomId!!
                        room_name.value = room.room_name!!
                        navController.navigate(Screens.PrivateChatScreen.route)
                    })
                {
                    /* TODO Reimplement the image bubble */
                    Image(
                        painter = painterResource(id = R.drawable.avatar_placeholder),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(Modifier.padding(8.dp))

                    Column()
                    {
            Text(text = room.room_name!!,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(2.dp))

            Row()
            {
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(room.last_sender_user + ": ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                        append(room.last_message_content)
                    }
                }
                Text(text = annotatedString,
                    maxLines = 2
                )

            }
            Spacer(Modifier.padding(4.dp))
            Divider(thickness = 1.dp, color = Color.LightGray)
        }

    }

}

@Composable
fun ChatList(rooms: List<ChatRoom>, modifier: Modifier = Modifier, navController: NavHostController, select_room: MutableState<String>, room_name: MutableState<String>, query: MutableState<String>) {
    LazyColumn(modifier = modifier.testTag("LazyColumn").padding(bottom = 80.dp)) {
        items(rooms) { room ->
            if (query.value.isEmpty()) {
                chatRow(room, navController, select_room, room_name = room_name)
            } else if (room.room_name?.contains(query.value, ignoreCase = true) == true) {
                chatRow(room, navController, select_room, room_name = room_name)
            }
        }
    }
}

@Composable
fun CreateRoomButton(showDialog: MutableState<Boolean>, email: String, roomObserver: ChatRoomViewModel){
    FloatingActionButton(
        onClick = { showDialog.value = true },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.GroupAdd, contentDescription = "Create new room")
    }

    // Create room dialog
    CreateRoomDialog(showDialog, onRoomCreated = { newRoom ->
        roomObserver.createRoom(newRoom)
    }, email)
}
@Composable
fun CreateRoomDialog(
    showDialog: MutableState<Boolean>,
    onRoomCreated: (ChatRoom) -> Unit,
    email: String
) {
    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Create a new room", fontWeight = FontWeight.Bold, color = Color.Yellow)
                val roomNameState = rememberSaveable { mutableStateOf("") }
                TextField(
                    value = roomNameState.value,
                    onValueChange = { roomNameState.value = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clip(RoundedCornerShape(8.dp)),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { Log.d("Room Creation", "User entered a name")}
                    ),
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { showDialog.value = false },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }
                    Button(
                        enabled = roomNameState.value.isNotEmpty(),
                        onClick = {
                            if(roomNameState.value.isNotEmpty())
                            {
                                showDialog.value = false
                                val newMembers = mutableListOf<String>()
                                newMembers.add(email)
                                val currentTimeMillis = System.currentTimeMillis()
                                val currentTimeStamp = Timestamp(currentTimeMillis)
                                val newRoom = ChatRoom(
                                    room_name = roomNameState.value,
                                    user_list = newMembers,
                                    last_message_content = "It's empty here, start chatting!",
                                    last_sender_user = "System",
                                    last_sent_message_time = currentTimeStamp,
                                    roomId = "",
                                    room_profile_picture = URI("")
                                )
                                onRoomCreated(newRoom)
                            }
                        }
                    ) {
                        Text(text = "Create")
                    }
                }
            }
        }
    }
}

@Composable
fun showDialog(message: String) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Success") },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = { /* Handle button click if needed */ }) {
                Text(text = "OK")
            }
        }
    )
}


