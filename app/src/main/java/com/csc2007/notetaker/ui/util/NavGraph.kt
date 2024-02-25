package com.csc2007.notetaker.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.TopSearchBar
import com.csc2007.notetaker.ui.colors

@Composable
fun PrivateChatPage(navController: NavHostController, viewModel: UserViewModel = viewModel(), selected_chatter: Chatter, userId: Int){
// so far it assumes a single chatter with you, not grouped yet

    // change the sampleMessages to an observable list of messages from the database so
    // it doesn't depend on the runtime to render out the bubbles
    val sampleMessages = listOf(
        Message(content = "Hello!", from = selected_chatter),
        Message(senderId = userId, content = "What are you up to today?"),
        Message(content = "Bees", imageContent = R.drawable.bees, from = selected_chatter),
        Message(senderId = userId, content = "Lots of bees here", imageContent = R.drawable.lots_of_bees),
        Message(senderId = userId, content = "By the way did you see the Bee movie?"),
        Message(senderId = userId, content = "Apparently they did a Bee movie reboot, classic disney things."),
        Message(content = "Really? Didn't see that yet, where can I watch it?", from = selected_chatter),
        Message(senderId = userId, content = "You can just pirate the movie on watchMovies.com"),
        Message(content = "Oh wow thanks, really needed that", from = selected_chatter),
        Message(content = "Pirating is the way to go", from = selected_chatter),
        Message(content = "Nobody likes paying for actual content", from = selected_chatter),
        Message(content = "smile", from = selected_chatter),
    )

    val userInput = rememberSaveable{ mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize())
    {

        Column(modifier = Modifier.weight(1f))
        {
            TopNavBarText(navController = navController, title = selected_chatter.userName, imageDisplay = selected_chatter.imgDrawable, modifier = Modifier.fillMaxWidth()) // Remember to include the image at the right
            messageList(messages = sampleMessages, myUserId = userId, navController = navController)
        }

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(4.dp))
        {

            Spacer(Modifier.padding(8.dp))

            // add logic checks for even if input is empty but theres an image/file attached to allow sending
            inputBar(label = "Text message", userInput = userInput, modifier = Modifier.weight(0.6f), messages = sampleMessages)

            Box(modifier = Modifier
                .size(60.dp)
                .padding(end = 0.dp)
                .clickable {},
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
fun TextBubble(text: String, sender: Int, myUserId: Int)
{
    val backgroundColor =
        if(sender == myUserId) colors.primaryColor
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
fun messageRow(message: Message, myUserId: Int, navController: NavHostController)
{
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(message.senderId == myUserId) Arrangement.End else Arrangement.Start)
    {
        if(message.senderId != myUserId)
        {
            Image(
                painter = painterResource(id = message.from.imgDrawable),
                contentDescription = "profile picture",
                modifier = Modifier.padding(5.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if(message.senderId == myUserId) Alignment.End else Alignment.Start)
        {
            if(message.imageContent != null)
            {
                ImageBubble(drawableId = message.imageContent, navController = navController)
            }
            if(message.content != null)
            {
                TextBubble(text = message.content, sender = message.senderId, myUserId = myUserId)
            }
        }

    }


}

@Composable
fun messageList(messages: List<Message>, myUserId: Int, navController: NavHostController)
{
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.testTag("LazyColumn")
    ) {
        items(messages) { message ->
            messageRow(message, myUserId, navController)
        }
    }

    // Scroll to the latest message ONLY when the composable is first composed
    // LaunchedEffect(messages) { // if want to make it scroll to the bottom everytime new messages get appended (observed)
    LaunchedEffect(Unit) {
        listState.scrollToItem(messages.size - 1)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputBar(modifier: Modifier = Modifier, label: String, userInput: MutableState<String>, messages: List<Message>)
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
                    .clickable(enabled = userInput.value.isNotEmpty()) {/*TODO send the message*/ }
            )
        }
    )
}

@Composable
fun calculateMaxWidth(percentage: Float): Dp {
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
    return (screenWidth * percentage)
}

data class Message(
    val senderId: Int = 0,
    val from: Chatter = Chatter(id = 0,
        imgDrawable = R.drawable.kacie),
    val content: String? = null,
    val imageContent: Int? = null
// can add an image to the Message and render together
)

