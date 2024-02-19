package com.csc2007.notetaker.ui.signup

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBar

@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    email: MutableState<String> = mutableStateOf(""),
    username: MutableState<String> = mutableStateOf(""),
    password: MutableState<String> = mutableStateOf(""),
    confirmPassword: MutableState<String> = mutableStateOf("")
) {

    val ibmPlexFamily = FontFamily(
        Font(R.font.ibm_plex_mono_bold, FontWeight.Bold)
    )
    
    var result by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = modifier
    ) {

        TopNavBar(navController = navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

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
                    text = "Create New Account",
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
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text(text = "Username") },
                    modifier = Modifier.fillMaxWidth())

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text(text = "Password") },
                    modifier = Modifier.fillMaxWidth())

                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text(text = "Confirm Password") },
                    modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    result = RegisterUser(email, username, password, confirmPassword, viewModel)
                }) {
                    Text(text = "Register")
                }

                Spacer(modifier = Modifier.height(80.dp))

                Row {
                    Text(
                        text = "Already have an account?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium)
                    Text(
                        text = "Login Here",
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { navController.navigate("login_screen") })
                }

                if (result != null) {
                    if (result == false) {
                        ShowFailedSnackbar()
                    } else {
                        ShowPassedSnackbar()
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowFailedSnackbar() {
    Snackbar(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = "Failed to create user account")
    }
}

@Composable
private fun ShowPassedSnackbar() {
    Snackbar(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = "Successfully created user account")
    }
}

private fun RegisterUser(
    email: MutableState<String>,
    username: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    viewModel: UserViewModel) : Boolean {

    if (email.value.isEmpty() or username.value.isEmpty() or password.value.isEmpty() or confirmPassword.value.isEmpty()) {
        return false
    }

    if (password.value != confirmPassword.value) {
        return false
    }

    val newUser = User(email = email.value, userName = username.value, password = password.value)
    viewModel.insert(newUser)

    return true
}


@Preview(showBackground = true)
@Composable
fun SignUpPagePreview() {

    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SignUpPage()
        }
    }
}