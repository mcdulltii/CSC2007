package com.csc2007.notetaker.ui.chat

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.TopSearchBar
import com.csc2007.notetaker.ui.colors

@Composable
fun PrivateChatPage(navController: NavHostController, viewModel: UserViewModel = viewModel(), selected_chat: Chatter, userId: Int){
// so far it assumes a single chatter with you, not grouped yet

    var userInput = rememberSaveable{ mutableStateOf("")}

    Column(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.weight(1f))
        {
            TopNavBarText(navController = navController, title = selected_chat.userName, imageDisplay = selected_chat.imgDrawable, modifier = Modifier.fillMaxWidth()) // Remember to include the image at the right
//            messageList()
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(16.dp))
        {
            Spacer(Modifier.padding(8.dp))
            inputBar(label = "Text message", userInput = userInput, modifier = Modifier.weight(0.9f))
            Box(modifier = Modifier.size(60.dp).padding(end = 0.dp).clickable{},
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
    //color = MaterialTheme.colorScheme.onSecondary for non my id
    // text bubble then
}

@Composable
fun messageRow(message: Message, myUserId: Int)
{

}

@Composable
fun messageList(messages: List<Message>, myUserId: Int)
{

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputBar(modifier: Modifier = Modifier, label: String, userInput: MutableState<String>)
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
                modifier = Modifier.size(30.dp).clickable(enabled = userInput.value.isNotEmpty()){/*TODO send the message*/}
            )
        }
    )
}

data class Message(
    val sender: Int = 0,
    val content: String = "Hello!"
)