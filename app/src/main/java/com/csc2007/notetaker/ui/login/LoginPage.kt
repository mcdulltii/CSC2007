package com.csc2007.notetaker.ui.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBar

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    email: MutableState<String> = mutableStateOf(""),
    password: MutableState<String> = mutableStateOf(""),
    loggedIn: MutableState<Boolean?> = mutableStateOf(false)
) {

    val ibmPlexFamily = FontFamily(
        Font(R.font.ibm_plex_mono_bold, FontWeight.Bold)
    )

    val loggedInState by viewModel.loggedIn.collectAsState()

    if (loggedInState == true) {
        loggedIn.value = loggedInState
        navController.navigate("modules_screen")
    }

    Column(modifier = modifier) {
        TopNavBar(navController = navController)
        
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
                    text = "Login Here Now",
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
                    viewModel.login(email.value, password.value)
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
                        modifier = Modifier.clickable { navController.navigate("signup_screen") })
                }

                if (loggedInState == false) {
                    ShowFailedSnackbar()
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

    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginPage()
        }
    }
}