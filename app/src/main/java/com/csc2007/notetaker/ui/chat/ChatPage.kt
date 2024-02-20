package com.csc2007.notetaker.ui.chat

import android.graphics.drawable.shapes.RoundRectShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.TopNavBar
import com.csc2007.notetaker.ui.TopSearchBar
import com.csc2007.notetaker.ui.util.BottomBar

@Composable
fun ChatPage(navController: NavHostController,
             viewModel: UserViewModel = viewModel()) {

//    val users by viewModel.allUsers.collectAsState()
    var sampleChatters = listOf(
        Chatter(id = 0,
                userName = "Kacie",
                lastSentTo = "Sandra Adams",
                latestText = " - It’s the one week of the year in which you get the chance to take…",
                imgDrawable = R.drawable.kacie),
        Chatter(id = 1,
                userName = "Tommy",
                lastSentTo = "Ray Neal",
                latestText = " - Healthy, robust, contracting, healthy, robust and contracting like a lung.",
                imgDrawable = R.drawable.tommy
        ),
        Chatter(id = 2,
                userName = "Princess",
                lastSentTo = "Carrie Mann",
                latestText = " - A wonderful serenity has taken possession of my entire soul, like…",
                imgDrawable = R.drawable.princess
        ),
        Chatter(id = 3,
            userName = "Magical Girl",
            lastSentTo = "Lelia Colon",
            latestText = " - Speaking of which, Peter really wants you to come in on Friday.",
            imgDrawable = R.drawable.magical_girl
            ),
        Chatter(id = 4,
            userName = "Emperor Of Meow",
            lastSentTo = "to Trevor, Andrew, Sandra",
            latestText = " - Images span the screen in ribbons, which expand with ",
            imgDrawable = R.drawable.emperor_of_meow)
    )

    Column(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier.padding(6.dp))
            TopSearchBar()
            Spacer(modifier = Modifier.padding(6.dp))

            ChatList(chats = sampleChatters)
        }
        Column(verticalArrangement = Arrangement.Bottom)
        {
            BottomNavBar()
        }
    }

}

@Composable
fun chatRow(chatter: Chatter)
{
    Row(modifier = Modifier.padding(8.dp).clickable{/*TODO implement navigation to this specific chat*/})
    {
        Image(
        painter = painterResource(id = chatter.imgDrawable),
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.padding(8.dp))

        Column()
        {
            Text(text = chatter.userName,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(2.dp))

            Row()
            {
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(chatter.lastSentTo)
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                        append(chatter.latestText)
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
fun ChatList(chats: List<Chatter>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.testTag("LazyColumn")){
        items(chats){
                chat -> chatRow(chat)
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



