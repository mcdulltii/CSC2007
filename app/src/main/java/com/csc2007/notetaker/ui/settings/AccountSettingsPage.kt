package com.csc2007.notetaker.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: UserViewModel = viewModel()
) {

    var loggedInUser = viewModel.loggedInUser.collectAsState().value

    var id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0) }
    var email = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.email else "") }
    var username = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.userName else "") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }

    Column(modifier = modifier) {

        TopNavBarText(navController = navController, title = "Account Settings")

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(50.dp)))

                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        ElevatedButton(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                            contentPadding = PaddingValues(3.5.dp),
                            modifier = Modifier
                                .size(21.dp)) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Profile Picture",
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Emperor of Meow",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.15.sp,
                        lineHeight = 24.sp)

                    Text(
                        text = "@EmperorOfMeow",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp,
                        letterSpacing = 0.15.sp)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = "Username") },
                modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text(text = "Confirm Password") },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                      if (password.value.isEmpty() or confirmPassword.value.isEmpty()) {
                          viewModel.updateEmailAndUserName(email.value, username.value, id.value)
                      }
                },
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Save Changes")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Sign Out")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountSettingsPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AccountSettingsPage()
        }
    }
}