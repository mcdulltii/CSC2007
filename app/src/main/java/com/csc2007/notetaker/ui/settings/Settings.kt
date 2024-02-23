package com.csc2007.notetaker.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.TopSearchBar
import com.csc2007.notetaker.ui.util.Screens

@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    Column(modifier = modifier) {

        TopNavBarText(navController = navController, title = "Settings")


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopSearchBar()

            Spacer(modifier = Modifier.height(15.dp))

            ListItem(
                headlineContent = { Text(text = "Account") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.PersonOutline, contentDescription = "Account Icon")
                },
                modifier = Modifier.clickable { navController.navigate(Screens.AccountSettingsScreen.route) }
            )

            ListItem(
                headlineContent = { Text(text = "Notifications") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.Alarm, contentDescription = "Notification Icon")
                },
                modifier = Modifier.clickable { navController.navigate(Screens.NotificationsSettingsScreen.route) }
            )

            ListItem(
                headlineContent = { Text(text = "Pomodoro") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.HourglassBottom, contentDescription = "Pomodoro Icon")
                },
                modifier = Modifier.clickable { /** TODO **/ }
            )

            ListItem(
                headlineContent = { Text(text = "Appearance") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.RemoveRedEye, contentDescription = "Appearance Icon")
                },
                modifier = Modifier.clickable { /** TODO **/ }
            )

            ListItem(
                headlineContent = { Text(text = "Privacy & Security") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.Lock, contentDescription = "Privacy & Security Icon")
                },
                modifier = Modifier.clickable { /** TODO **/ }
            )

            ListItem(
                headlineContent = { Text(text = "Help & Support") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.Headphones, contentDescription = "Help & Support Icon")
                },
                modifier = Modifier.clickable { /** TODO **/ }
            )

            ListItem(
                headlineContent = { Text(text = "About") },
                trailingContent = {
                    Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                },
                leadingContent = {
                    Icon(Icons.Default.QuestionMark, contentDescription = "About Icon")
                },
                modifier = Modifier.clickable { /** TODO **/ }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController = navController)

    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsPage()
        }
    }
}