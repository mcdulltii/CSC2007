package com.csc2007.notetaker.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
//    val users by viewModel.allUsers.collectAsState()
    val thisUsersRooms = rememberSaveable{mutableStateOf(emptyList<ChatRoom>())}

    // Attach snapshot listener
    LaunchedEffect(Unit) {
        roomObserver.getAllRooms(roomsUserIsIn = thisUsersRooms, userEmail = email)
    }

    Column(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier.padding(6.dp))
            TopSearchBar(search = searchQuery, isActive = searchIsActive)
            Spacer(modifier = Modifier.padding(6.dp))

            ChatList(rooms = thisUsersRooms.value, navController = navController, select_room = selectedRoomID, room_name = selectedRoomName, query = searchQuery)
        }
        Column(verticalArrangement = Arrangement.Bottom)
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
    LazyColumn(modifier = modifier.testTag("LazyColumn")) {
        items(rooms) { room ->
            if (query.value.isEmpty()) {
                chatRow(room, navController, select_room, room_name = room_name)
            } else if (room.room_name?.contains(query.value, ignoreCase = true) == true) {
                chatRow(room, navController, select_room, room_name = room_name)
            }
        }
    }
}

data class Chatter(
    val id: Int = 0,
    val userName: String = "Kacie",
    val lastSentTo: String = "Sandra Adams - ",
    val latestText: String = "It’s the one week of the year in which you get the chance to take…",
    val imgDrawable: Int = R.drawable.kacie
)

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun searchBar(modifier: Modifier = Modifier, label: String, searchInput: MutableState<String>)
//{
//    TextField(
//        value = searchInput.value,
//        onValueChange = { searchInput.value = it },
//        singleLine = true,
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        label = {Text(label)},
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search",
//                tint = MaterialTheme.colorScheme.primary // change to our color scheme
//            )
//        },
//        maxLines = 1,
//        shape = RoundedCornerShape(8.dp),
//        colors = TextFieldDefaults.textFieldColors(
//            unfocusedIndicatorColor = Color.Transparent,
//            cursorColor = MaterialTheme.colorScheme.primary // change to our color scheme
//        )
//    )
//}

