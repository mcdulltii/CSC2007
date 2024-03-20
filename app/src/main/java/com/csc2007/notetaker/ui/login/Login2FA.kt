package com.csc2007.notetaker.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.util.Screens
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import kotlinx.coroutines.delay
import org.apache.commons.codec.binary.Base32

@Composable
fun Login2FA(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {

    val secret = userViewModel.loggedInUserSecret.collectAsState().value
    val googleAuthenticator = GoogleAuthenticator(Base32().encode(secret))
    var code = googleAuthenticator.generate()

    var validOTP = remember { mutableStateOf<Boolean?>(null) }

    var enteredOTP = remember { mutableStateOf("") }

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(modifier = modifier) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Enter your OTP Code from Google Authenticator",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(20.dp))

                TextField(value = enteredOTP.value, onValueChange = { enteredOTP.value = it })

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    if (verifyOTP(enteredOTP.value, code)) {
                        validOTP.value = true
                        navController.navigate(Screens.ModulesScreen.route)
                    } else {
                        validOTP.value = false
                    }
                }) {
                    Text(text = "Enter")
                }

                Button(onClick = {
                    userViewModel.logout()
                    navController.navigate(Screens.LoginScreen.route)
                }) {
                    Text(text = "Cancel")
                }

                if (validOTP.value == false) {
                    ShowSnackbar(msg = "Invalid OTP entered!")
                    LaunchedEffect(Unit) {
                        delay(1000)
                        validOTP.value = null
                    }
                }
            }
        }
    } else {

        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter your OTP Code from Google Authenticator",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(value = enteredOTP.value, onValueChange = { enteredOTP.value = it })

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        if (verifyOTP(enteredOTP.value, code)) {
                            validOTP.value = true
                            navController.navigate(Screens.ModulesScreen.route)
                        } else {
                            validOTP.value = false
                        }
                    }) {
                        Text(text = "Enter")
                    }

                    Button(onClick = {
                        userViewModel.logout()
                        navController.navigate(Screens.LoginScreen.route)
                    }) {
                        Text(text = "Cancel")
                    }

                    if (validOTP.value == false) {
                        ShowSnackbar(msg = "Invalid OTP entered!")
                        LaunchedEffect(Unit) {
                            delay(1000)
                            validOTP.value = null
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


fun verifyOTP(userOTP: String, serverOTP: String): Boolean {

    if (userOTP != serverOTP) {
        return false
    }

    return true
}