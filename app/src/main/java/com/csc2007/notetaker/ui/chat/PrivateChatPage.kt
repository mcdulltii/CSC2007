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
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.ChatMessage
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.colors
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PrivateChatPage(navController: NavHostController, viewModel: UserViewModel, firestore_db: FirebaseFirestore, selected_chatter: Chatter, userId: Int){
// so far it assumes a single chatter with you, not grouped yet                                                  selected chatter will be changed to the room id

    // change the sampleMessages to an observable list of messages from the database so
    // it doesn't depend on the runtime to render out the bubbles
//    val sampleMessages = listOf(
//        Message(content = "Hello!", from = selected_chatter),
//        Message(senderId = userId, content = "What are you up to today?"),
//        Message(content = "Bees", imageContent = R.drawable.bees, from = selected_chatter),
//        Message(senderId = userId, content = "Lots of bees here", imageContent = R.drawable.lots_of_bees),
//        Message(senderId = userId, content = "By the way did you see the Bee movie?"),
//        Message(senderId = userId, content = "Apparently they did a Bee movie reboot, classic disney things."),
//        Message(content = "Really? Didn't see that yet, where can I watch it?", from = selected_chatter),
//        Message(senderId = userId, content = "You can just pirate the movie on watchMovies.com"),
//        Message(content = "Oh wow thanks, really needed that", from = selected_chatter),
//        Message(content = "Pirating is the way to go", from = selected_chatter),
//        Message(content = "Nobody likes paying for actual content", from = selected_chatter),
//        Message(content = "smile", from = selected_chatter),
//    )

//    val messages_in_room = ArrayList<ChatMessage>()
//
//    val docRef = firestore_db.collection("ChatMessages")
//    docRef.addSnapshotListener { value, e ->
//            if (e != null) {
//                Log.w("observer", "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            for (doc in value!!) {
//                var sender_user: String? = null
//                var sender_email: String? = null
//                var content: String? = null
//                var time_stamp: Timestamp? = null
//                doc.getString("sender_user")?.let {
//                    sender_user = it
//                }
//                doc.getString("sender_email")?.let{
//                    sender_email = it
//                }
//                doc.getString("content")?.let{
//                    content = it
//                }
////                doc.getString("time_stamp")?.let{
////                    time_stamp = it
////                }
//                messages_in_room.add(ChatMessage(sender_user = sender_user, sender_email = sender_email, content = content, time_stamp = null, room_id_belong = null, image = null))
//            }
//            Log.d("observer", "Currently there are ${messages_in_room.size} many messages")
//        }

    // Initialize messages state
    val messages_in_room = remember{ mutableStateOf(emptyList<ChatMessage>()) }
    val listState = rememberLazyListState()
    // Attach snapshot listener
    LaunchedEffect(Unit) {
        val messages = mutableListOf<ChatMessage>()
        val docRef = firestore_db.collection("ChatMessages").orderBy("time_stamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("observer", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    var sender_user: String? = null
                    var sender_email: String? = null
                    var content: String? = null
                    var time_stamp: Timestamp? = null
                    doc.getString("sender_user")?.let {
                        sender_user = it
                    }
                    doc.getString("sender_email")?.let{
                        sender_email = it
                    }
                    doc.getString("content")?.let{
                        content = it
                    }
//                    doc.getString("time_stamp")?.let { timeStampString ->
//                        // Parse timeStampString to Date
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//                        val date = dateFormat.parse(timeStampString)
//
//                        // Convert Date to Timestamp
//                        time_stamp = date?.let { Timestamp(it.time) }
//                    }
                    val newMessage = ChatMessage(sender_user = sender_user, sender_email = sender_email, content = content, time_stamp = time_stamp, room_id_belong = null, image = null)

                    // Add the new message to the list if it's not already present
                    if (!messages.contains(newMessage)) {
                        messages.add(newMessage)
                    }
                }

                // Update messages state with only the new messages
                messages_in_room.value = messages.toList()
                Log.d("observer", "Currently there are ${messages.size} many messages")

            }
    }

    val username by viewModel.loggedInUserUsername.collectAsState()
    val email by viewModel.loggedInUserEmail.collectAsState()

    val userInput = rememberSaveable{ mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize())
    {

        Column(modifier = Modifier.weight(1f))
        {
            TopNavBarText(navController = navController, title = selected_chatter.userName, imageDisplay = selected_chatter.imgDrawable, modifier = Modifier.fillMaxWidth()) // Remember to include the image at the right
            messageList(messages = messages_in_room.value, my_email = email, navController = navController, listState = listState)
        }

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(4.dp))
        {

            Spacer(Modifier.padding(8.dp))

            // add logic checks for even if input is empty but theres an image/file attached to allow sending
            inputBar(label = "Text message", userInput = userInput, username = username, email = email, modifier = Modifier.weight(0.6f),/* messages = messages_in_room, */firestore_db = firestore_db)

            Box(modifier = Modifier
                .size(60.dp)
                .padding(end = 0.dp)
                .clickable {
                    val currentTimeMillis = System.currentTimeMillis()
                    val currentTimeStamp = Timestamp(currentTimeMillis)
                    val message = ChatMessage(
                        sender_user = "Kacie",
                        sender_email = "Kacie123@hotmail.com",
                        time_stamp = currentTimeStamp,
                        content = userInput.value,
                        room_id_belong = 1,
                        image = null
                    )
                    firestore_db
                        .collection("ChatMessages")
                        .document()
                        .set(message)
                        .addOnSuccessListener {
                            Log.d(
                                "Sucessfully insert",
                                "DocumentSnapshot successfully written!"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                "Failed to insert",
                                "Error writing document",
                                e
                            )
                        }

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
fun messageRow(message: ChatMessage, my_email: String, navController: NavHostController)
{
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(message.sender_email == my_email) Arrangement.End else Arrangement.Start)
    {
        if(message.sender_email != my_email)
        {
//            Image(
//                painter = painterResource(id = message.from.imgDrawable),
//                contentDescription = "profile picture",
//                modifier = Modifier.padding(5.dp)
//            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if(message.sender_email == my_email) Alignment.End else Alignment.Start)
        {
//            if(message.imageContent != null)
//            {
//                ImageBubble(drawableId = message.imageContent, navController = navController)
//            }
            if(message.content != null)
            {
                TextBubble(text = message.content, sender_email = message.sender_email!!, my_email = my_email)
            }
        }

    }


}

@Composable
fun messageList(messages: List<ChatMessage>, my_email: String, navController: NavHostController, listState: LazyListState)
{

    LazyColumn(
        state = listState,
        modifier = Modifier.testTag("LazyColumn")
    ) {
        items(messages) { message ->
            messageRow(message, my_email, navController)
        }
    }

    if (messages.isNotEmpty()) {
        LaunchedEffect(messages) {
//            listState.scrollToItem(messages.size - 1)
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    // Scroll to the latest message ONLY when the composable is first composed
    // LaunchedEffect(messages) { // if want to make it scroll to the bottom everytime new messages get appended (observed)
//    LaunchedEffect(Unit) {
//        listState.scrollToItem(messages.size - 1)
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputBar(modifier: Modifier = Modifier, username: String, email: String, label: String, userInput: MutableState<String>, /*messages: List<Message>, */firestore_db: FirebaseFirestore, room_id: Int = 0)
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
                        val currentTimeMillis = System.currentTimeMillis()
                        val currentTimeStamp = Timestamp(currentTimeMillis)
                        val message = ChatMessage(
                            sender_user = username,
                            sender_email = email,
                            time_stamp = currentTimeStamp,
                            content = userInput.value,
                            room_id_belong = 1, // TODO change to room id
                            image = null
                        )
                        insert_into_db(message = message, firestore_db = firestore_db)

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

fun insert_into_db(message: ChatMessage, firestore_db: FirebaseFirestore)
{
    firestore_db
        .collection("ChatMessages")
        .document()
        .set(message)
        .addOnSuccessListener {
            Log.d(
                "Sucessfully insert",
                "DocumentSnapshot successfully written!"
            )
        }
        .addOnFailureListener { e ->
            Log.w(
                "Failed to insert",
                "Error writing document",
                e
            )
        }
}
//data class Message(
//    val senderId: Int = 0,
//    val from: Chatter = Chatter(id = 0,
//        imgDrawable = R.drawable.kacie),
//    val content: String? = null,
//    val imageContent: Int? = null
//// can add an image to the Message and render together
//)