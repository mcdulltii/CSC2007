package com.csc2007.notetaker.ui.chat

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.ChatMessage
import com.csc2007.notetaker.database.ChatRoom
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatMessageViewModel
import com.csc2007.notetaker.database.viewmodel.chat_room.ChatRoomViewModel
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.colors
import com.csc2007.notetaker.ui.util.Screens
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URI
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import android.app.DownloadManager
import android.os.Environment
import android.widget.Toast

@Composable
fun PrivateChatPage(navController: NavHostController, viewModel: UserViewModel, firestore_db: FirebaseFirestore, roomName: String, roomId: String)
{
    // Init viewModel observer for this chat
    val chatObserver = ChatMessageViewModel(firestore_db = firestore_db, RoomId = roomId) // TODO change this to the actual room ID
    val roomObserver = ChatRoomViewModel(firestore_db = firestore_db)
    // Init messages state
    val messages_in_room = remember{ mutableStateOf(emptyList<ChatMessage>()) }
    // not sure what happens if change to different room, hopefully only renders the room specific ones. Should be fine though
    val listState = rememberLazyListState()
    val currentlyEditingMessageId = rememberSaveable{ mutableStateOf("")}
    val context = LocalContext.current
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
            TopNavBarText(navController = navController,
                title = roomName,
                imageDisplay = R.drawable.avatar_placeholder,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screens.EditChatRoomScreen.route) },
            ) // Remember to include the image at the right
            MessageList(messages = messages_in_room.value, myEmail = email, navController = navController, listState = listState, userInput = userInput, messageIdToEdit = currentlyEditingMessageId, chatObserver = chatObserver, context = context)

        }

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(4.dp))
        {

            Spacer(Modifier.padding(8.dp))

            // add logic checks for even if input is empty but theres an image/file attached to allow sending
            InputBar(label = "Text message", userInput = userInput, username = username, myEmail = email, modifier = Modifier.weight(0.6f), chatObserver = chatObserver, room_id = roomId, editing_message_id = currentlyEditingMessageId)

            Box(modifier = Modifier
                .size(60.dp)
                .padding(end = 0.dp)
                .clickable {
                    // use camera gallery(?) idk
                    /* TODO  handle file upload and image upload */
                },
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
fun pdfBubble(message: ChatMessage, context: Context)
{
    val maxWidthPercentage = 0.5f
    val maxWidth = calculateMaxWidth(maxWidthPercentage)
    Box(
        modifier = Modifier
            .size(maxWidth, 120.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(25.dp))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = "${message.content!!}.pdf", textAlign = TextAlign.Center)
            Image(
                painter = painterResource(id = R.drawable.pdf_image),
                contentDescription = "pdf file",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        downloadFile(message.pdf_link.toString(), context, message.content!!)
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}


fun downloadFile(uri: String, context: Context, pdfName: String) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadUri = Uri.parse(uri)

    val request = DownloadManager.Request(downloadUri)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setAllowedOverRoaming(false)
        .setTitle("${pdfName}.pdf")
        .setDescription("Downloading PDF file")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${pdfName}.pdf")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

    downloadManager.enqueue(request)
    Toast.makeText(context, "Downloading PDF file...", Toast.LENGTH_SHORT).show()
}


@Composable
fun TextBubble(text: String, sender_email: String, my_email: String, message: ChatMessage, userInput: MutableState<String>, messageIdToEdit: MutableState<String>, chatObserver: ChatMessageViewModel)
{
    val showDialog = rememberSaveable{ mutableStateOf(false)}
    val backgroundColor =
        if(sender_email == my_email) colors.primaryColor
        else MaterialTheme.colorScheme.secondary

    val textColor = Color.White

    val maxWidthPercentage = 0.65f
    val maxWidth = calculateMaxWidth(maxWidthPercentage)

    Box(
        modifier = Modifier
            .clickable {
                showDialog.value = true
            }
            .widthIn(max = maxWidth)
            .padding(8.dp)
            .background(
                backgroundColor, shape = RoundedCornerShape(25.dp)
            )
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
    if(sender_email == my_email)
    {
        ShowEditOrDelete(showDialog = showDialog, message = message, messageIdToEdit = messageIdToEdit, userInput = userInput, chatObserver = chatObserver)
    }
}

@Composable
fun ShowEditOrDelete(showDialog: MutableState<Boolean>, message: ChatMessage, messageIdToEdit: MutableState<String>, userInput: MutableState<String>, chatObserver: ChatMessageViewModel)
{
    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Edit or Delete Message?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Row()
                {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            val currentTimeMillis = System.currentTimeMillis()
                            val currentTimeStamp = Timestamp(currentTimeMillis)
                            chatObserver.delete(message.message_id!!) // Just clear after deleting
                            chatObserver.updateLastSent(content ="${message.sender_user!!} deleted a message", time_stamp = currentTimeStamp, user = message.sender_user!!)
                            showDialog.value = false
                        }
                    ) {
                        Text(text = "Delete")
                    }

                    Button(

                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            messageIdToEdit.value = message.message_id!!
                            userInput.value = message.content!!
                            showDialog.value = false
                        }
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        }
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
            EnlargeImage(image = drawableId, showOverlay = showOverlay, navController = navController)
        }
    }
}

@Composable
fun EnlargeImage(image: Int, showOverlay: MutableState<Boolean>, navController: NavHostController)
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
fun MessageRow(message: ChatMessage, myEmail: String, navController: NavHostController, userInput: MutableState<String>, messageIdToEdit: MutableState<String>, chatObserver: ChatMessageViewModel, context: Context)
{
    /* TODO re-implement the profile pictures once the database modeling has been done */
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(message.sender_email == myEmail) Arrangement.End else Arrangement.Start)
    {
        if(message.sender_email != myEmail)
        {
            Image(
                painter = painterResource(id = R.drawable.avatar_placeholder),
                contentDescription = "profile picture",
                modifier = Modifier
                    .padding(top = 25.dp, start = 5.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if(message.sender_email == myEmail) Alignment.End else Alignment.Start)
        {


            if(message.sender_email != myEmail)
            {
                Text(text = "${message.sender_user!!}@${convertTimestampToTime(message.time_stamp!!)}", modifier = Modifier.padding(start = 8.dp), fontSize = 12.sp)
            }
            else Text(convertTimestampToTime(message.time_stamp!!), modifier = Modifier.padding(end = 8.dp), fontSize = 12.sp)
            if(message.pdf_link != null)
            {
                pdfBubble(message = message, context = context)
            }
            else
            {
                TextBubble(text = message.content!!, sender_email = message.sender_email!!, my_email = myEmail, message = message, userInput = userInput, messageIdToEdit = messageIdToEdit, chatObserver = chatObserver)
            }

        }

    }
}

@Composable
fun MessageList(messages: List<ChatMessage>, myEmail: String, navController: NavHostController, listState: LazyListState, userInput: MutableState<String>, messageIdToEdit: MutableState<String>, chatObserver: ChatMessageViewModel, context: Context)
{

    LazyColumn(
        state = listState,
        modifier = Modifier.testTag("LazyColumn")
    ) {
        items(messages) { message ->
            MessageRow(message, myEmail, navController, userInput, messageIdToEdit, chatObserver = chatObserver, context = context)
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
fun InputBar(modifier: Modifier = Modifier, username: String, myEmail: String, label: String, userInput: MutableState<String>, chatObserver: ChatMessageViewModel, room_id: String, editing_message_id: MutableState<String>)
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
                            pdf_link = null
                        )
                        if (editing_message_id.value.isNotEmpty()) {
                            chatObserver.update(
                                updatedMessage = userInput.value,
                                message_id = editing_message_id.value
                            )
                            chatObserver.updateLastSent(
//                                room_id = room_id,
                                content = "$username edited a message",
                                time_stamp = currentTimeStamp,
                                user = "System"
                            )
                        } else {
                            chatObserver.insert(message = message, room_id = room_id)
                            chatObserver.updateLastSent(
//                                room_id = room_id,
                                content = userInput.value,
                                time_stamp = currentTimeStamp,
                                user = username
                            )
                        }

                        Log.d("messages", "Message being sent, ${userInput.value}")
                        editing_message_id.value =
                            "" // Clear the currently editing message_id if any
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

fun convertTimestampToTime(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(timestamp.time)
}