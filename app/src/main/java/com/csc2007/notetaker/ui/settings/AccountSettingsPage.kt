package com.csc2007.notetaker.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass
import com.csc2007.notetaker.ui.util.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: UserViewModel = viewModel(),
    window: WindowSizeClass = rememberWindowSizeClass()
) {

    val loggedInUser = viewModel.loggedInUser.collectAsState().value

    val id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0) }
    val email = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.email else "") }
    val username = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.userName else "") }

    val scrollState = rememberScrollState()

    if (AppTheme.orientation == Orientation.Portrait) {
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

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (email.value.isNotEmpty() or username.value.isNotEmpty()) {
                            viewModel.updateEmailAndUserName(email.value, username.value, id.value)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Save Changes")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate(Screens.ChangePasswordScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Change Password")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.logout()
                        navController.popBackStack(Screens.LandingScreen.route, inclusive = false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Sign Out")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomNavBar(navController = navController)
        }
    } else {
        Column(modifier = modifier) {
            
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.78f)
                    .verticalScroll(scrollState)
            ) {
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

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (email.value.isNotEmpty() or username.value.isNotEmpty()) {
                                viewModel.updateEmailAndUserName(email.value, username.value, id.value)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Save Changes")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            navController.navigate(Screens.ChangePasswordScreen.route)
                        },
                        modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Change Password")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.logout()
                            navController.popBackStack(Screens.LandingScreen.route, inclusive = false)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary),
                        modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Sign Out")
                    }
                    
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                BottomNavBar(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountSettingsPagePreview() {
    val window = rememberWindowSizeClass()

    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AccountSettingsPage()
        }
    }
}