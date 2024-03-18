package com.csc2007.notetaker.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText
import kotlinx.coroutines.delay

@Composable
fun ChangePasswordPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: UserViewModel = viewModel()
) {

    val loggedInUser = viewModel.loggedInUser.collectAsState().value

    val id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0 ) }

    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val result = remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState)
    ) {

        TopNavBarText(navController = navController, title = "Change Password")

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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

            Button(onClick = {
                result.value = ChangePassword(id, password, confirmPassword, viewModel)
            },
            modifier = Modifier.fillMaxWidth()) {
                Text(text = "Change Password")
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        if (result.value != null) {
            ShowSnackBar(msg = result.value!!)
            LaunchedEffect(Unit) {
                delay(2000)
                result.value = null
            }
        }

        BottomNavBar(navController = navController)
    }
}

@Composable
private fun ShowSnackBar(msg: String) {
    Snackbar(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = msg)
    }
}

private fun ChangePassword(
    id: MutableState<Int>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    viewModel: UserViewModel
): String {

    if (password.value.isEmpty() or confirmPassword.value.isEmpty()) {
        return "Password or Confirm Password field cannot be empty"
    }

    if (password.value != confirmPassword.value) {
        return "Password and Confirm Password does not match"
    }

    viewModel.updatePassword(password.value, id.value)

    return "Successfully updated password"
}


@Preview(showBackground = true)
@Composable
fun ChangePasswordPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChangePasswordPage()
        }
    }
}
