package com.csc2007.notetaker.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText

@Composable
fun NotificationsSettingsPage(modifier: Modifier = Modifier, navController: NavController = rememberNavController()) {

    var notifications = remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        TopNavBarText(navController = navController, title = "Notifications")

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
             Row(
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Switch(checked = notifications.value, onCheckedChange = { notifications.value = !notifications.value })

                 Spacer(modifier = Modifier.width(16.dp))

                 Text(text = "Allow Notifications")
             }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsSettingsPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NotificationsSettingsPage()
        }
    }
}