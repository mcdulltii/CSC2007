package com.csc2007.notetaker.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.TopNavBar
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass
import com.csc2007.notetaker.ui.util.Screens

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    window: WindowSizeClass = rememberWindowSizeClass()
) {

    val ibmPlexFamily = FontFamily(
        Font(R.font.ibm_plex_mono_bold, FontWeight.Bold)
    )

    val loggedInState by userViewModel.loggedIn.collectAsState()

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    if (loggedInState == true) {
        navController.navigate(Screens.ChatScreen.route)
    }


    if (AppTheme.orientation == Orientation.Portrait) {
        Column(modifier = modifier) {
            TopNavBar(navController = navController, route = Screens.LandingScreen.route)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "NOTETAKER.",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = ibmPlexFamily)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Start))

                    Spacer(modifier = Modifier.height(25.dp))

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier.fillMaxWidth())

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text(text = "Password") },
                        modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        userViewModel.login(email.value, password.value)
                    }) {
                        Text(text = "Login")
                    }

                    Spacer(modifier = Modifier.height(80.dp))

                    Row {
                        Text(
                            text = "Don't have an Account?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium)
                        Text(
                            text = "Create Here",
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { navController.navigate(Screens.SignUpScreen.route) })
                    }

                    if (loggedInState == false) {
                        ShowFailedSnackbar()
                    }
                }
            }
        }
    } else {
        Column(modifier = modifier) {
            TopNavBar(navController = navController, route = Screens.LandingScreen.route)

            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NOTETAKER.",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ibmPlexFamily)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Start))

                    Spacer(modifier = Modifier.height(25.dp))

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier.fillMaxWidth())

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text(text = "Password") },
                        modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        userViewModel.login(email.value, password.value)
                    }) {
                        Text(text = "Login")
                    }

                    Spacer(modifier = Modifier.height(80.dp))

                    Row {
                        Text(
                            text = "Don't have an Account?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium)
                        Text(
                            text = "Create Here",
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { navController.navigate(Screens.SignUpScreen.route) })
                    }

                    if (loggedInState == false) {
                        ShowFailedSnackbar()
                    }
                }
            }
        }
    }

}

@Composable
private fun ShowFailedSnackbar() {
    Snackbar(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Incorrect Email or Password")
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {

    val window = rememberWindowSizeClass()

    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginPage()
        }
    }
}