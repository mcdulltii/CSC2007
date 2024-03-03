package com.csc2007.notetaker.ui.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.ChatMessage
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatMessageViewModel
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.colors
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp

@Composable
fun PrivateChatPage(navController: NavHostController, viewModel: UserViewModel, firestore_db: FirebaseFirestore, room_name: Chatter, userId: Int)
{
    // Init viewModel observer for this chat
    val chatObserver = ChatMessageViewModel(firestore_db = firestore_db, RoomId = "") // TODO change this to the actual room ID

    // Init messages state
    val messages_in_room = remember{ mutableStateOf(emptyList<ChatMessage>()) }
    // not sure what happens if change to different room, hopefully only renders the room specific ones. Should be fine though
    val listState = rememberLazyListState()

    // Attach snapshot listener
    LaunchedEffect(Unit) {
        chatObserver.getMessagesFromRoom(messages_in_room = messages_in_room)
    }

    // Init this pages' inputs and auxiliary viewModel observers
    val username by viewModel.loggedInUserUsername.collectAsState()
    val email by viewModel.loggedInUserEmail.collectAsState()

    val userInput = rememberSaveable{ mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize())
    {

        Column(modifier = Modifier.weight(1f))
        {
            TopNavBarText(navController = navController, title = room_name.userName, imageDisplay = room_name.imgDrawable, modifier = Modifier.fillMaxWidth()) // Remember to include the image at the right
            messageList(messages = messages_in_room.value, myEmail = email, navController = navController, listState = listState)
        }

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(4.dp))
        {

            Spacer(Modifier.padding(8.dp))

            // add logic checks for even if input is empty but theres an image/file attached to allow sending
            inputBar(label = "Text message", userInput = userInput, username = username, myEmail = email, modifier = Modifier.weight(0.6f), chatObserver = chatObserver)

            Box(modifier = Modifier
                .size(60.dp)
                .padding(end = 0.dp)
                .clickable {
                           // use camera gallery(?) idk
                           /* TODO  handle file upload and image upload */},
                contentAlignment = Alignment.Center)
            {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "File Upload",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun TextBubble(text: String, sender_email: String, my_email: String)
{
    val backgroundColor =
        if(sender_email == my_email) colors.primaryColor
        else MaterialTheme.colorScheme.secondary

    val textColor = Color.White

    val maxWidthPercentage = 0.65f
    val maxWidth = calculateMaxWidth(maxWidthPercentage)

    Box(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .padding(8.dp)
            .background(backgroundColor, shape = RoundedCornerShape(25.dp))
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageBubble(drawableId: Int, navController: NavHostController)
{
    var showOverlay = rememberSaveable{ mutableStateOf(false) }
    val maxWidthPercentage = 0.5f
    val maxWidth = calculateMaxWidth(maxWidthPercentage)
    Box(
        modifier = Modifier
            .size(maxWidth, 120.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(25.dp))
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "image message",
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    showOverlay.value = true
                },
            contentScale = ContentScale.Fit
        )
        if(showOverlay.value) // bugged lmao
        {
            enlargeImage(image = drawableId, showOverlay = showOverlay, navController = navController)
        }
    }
}

@Composable
fun enlargeImage(image: Int, showOverlay: MutableState<Boolean>, navController: NavHostController)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent black background
            .clickable {
                showOverlay.value = false // Dismiss the overlay when clicked
            }
    ) {
        // Larger image content
        Image(
            painter = painterResource(id = image),
            contentDescription = "image message",
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun messageRow(message: ChatMessage, myEmail: String, navController: NavHostController)
{
    /* TODO re-implement the profile pictures once the database modeling has been done */
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(message.sender_email == myEmail) Arrangement.End else Arrangement.Start)
    {
        if(message.sender_email != myEmail)
        {
//            Image(
//                painter = painterResource(id = message.from.imgDrawable),
//                contentDescription = "profile picture",
//                modifier = Modifier.padding(5.dp)
//            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if(message.sender_email == myEmail) Alignment.End else Alignment.Start)
        {
//            if(message.imageContent != null)
//            {
//                ImageBubble(drawableId = message.imageContent, navController = navController)
//            }
            if(message.content != null)
            {
                TextBubble(text = message.content, sender_email = message.sender_email!!, my_email = myEmail)
            }
        }

    }


}

@Composable
fun messageList(messages: List<ChatMessage>, myEmail: String, navController: NavHostController, listState: LazyListState)
{

    LazyColumn(
        state = listState,
        modifier = Modifier.testTag("LazyColumn")
    ) {
        items(messages) { message ->
            messageRow(message, myEmail, navController)
        }
    }

    if (messages.isNotEmpty()) {
        //    LaunchedEffect(Unit) {// <-- one time function
        LaunchedEffect(messages) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputBar(modifier: Modifier = Modifier, username: String, myEmail: String, label: String, userInput: MutableState<String>, chatObserver: ChatMessageViewModel)
{
        TextField(
        value = userInput.value,
        onValueChange = { userInput.value = it },
        singleLine = false,
        modifier = modifier,
        label = {Text(label)},
        maxLines = 5,
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = colors.primaryColor // change to our color scheme
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = if (userInput.value.isNotEmpty()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.LightGray
                },
                modifier = Modifier
                    .size(30.dp)
                    .clickable(enabled = userInput.value.isNotEmpty()) {
                        /* TODO implement logic to check whether or not the user is editing or not */
                        val currentTimeMillis = System.currentTimeMillis()
                        val currentTimeStamp = Timestamp(currentTimeMillis)
                        val message = ChatMessage(
                            message_id = null, // Not necessary here, just needed for the room last message (weird circular dependency?)
                            sender_user = username,
                            sender_email = myEmail,
                            time_stamp = currentTimeStamp,
                            content = userInput.value,
                            image = null
                        )
                        chatObserver.insert(message = message)

                        Log.d("messages", "Message being sent, ${userInput.value}")
                        userInput.value = "" // clear user input
                        Log.d("messages", "Clearing the userInput to send new message")
                    }
            )
        }
    )
}

@Composable
fun calculateMaxWidth(percentage: Float): Dp {
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
    return (screenWidth * percentage)
}
