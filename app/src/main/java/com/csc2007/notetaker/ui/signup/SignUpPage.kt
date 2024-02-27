package com.csc2007.notetaker.ui.signup

import android.util.Patterns.EMAIL_ADDRESS
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
import com.csc2007.notetaker.database.viewmodel.AvatarViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBar
import kotlinx.coroutines.delay

@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(),
    avatarViewModel: AvatarViewModel = viewModel(),
    navController: NavController = rememberNavController(),
) {

    val ibmPlexFamily = FontFamily(
        Font(R.font.ibm_plex_mono_bold, FontWeight.Bold)
    )

    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

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
                    text = "Sign Up",
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

                Button(
                    onClick = {
                        result = RegisterUser(email, username, password, confirmPassword, userViewModel, avatarViewModel)
                    },
                    enabled = email.value.isNotBlank() && username.value.isNotBlank() && password.value.isNotBlank() && confirmPassword.value.isNotBlank()
                ) {
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
                    ShowSnackbar(result!!)
                    if (result!!.contains("Success")) {
                        LaunchedEffect(Unit) {
                            delay(1000)
                            navController.navigate("login_screen")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowSnackbar(msg: String) {
    Snackbar(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = msg)
    }
}

private fun RegisterUser(
    email: MutableState<String>,
    username: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    userViewModel: UserViewModel,
    avatarViewModel: AvatarViewModel) : String {

    if (email.value.isEmpty() or username.value.isEmpty() or password.value.isEmpty() or confirmPassword.value.isEmpty()) {
        return "Invalid input"
    }

    if (!EMAIL_ADDRESS.matcher(email.value).matches()) {
        return "Invalid email address"
    }

    if (password.value != confirmPassword.value) {
        return "Password mismatch"
    }

    userViewModel.register(email.value, username.value, password.value)

    return "Successfully created user account"
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