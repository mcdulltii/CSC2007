package com.csc2007.notetaker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.R

@Composable
fun LandingPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    windowSizeClass: WindowSizeClass = rememberWindowSizeClass()) {

    val scrollState = rememberScrollState()

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.notetaker_logo),
                contentDescription = "Notetaker Logo",
                modifier = Modifier.size(412.dp))

            Button(
                onClick = { navController.navigate("login_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryColor),
                modifier = Modifier.width(380.dp)) {
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.onPrimary)
            }

            Button(
                onClick = { navController.navigate("signup_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.width(380.dp)) {
                Text(
                    text = "Register",
                    color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(1f)) {
                    Image(painter = painterResource(id = R.drawable.notetaker_logo),
                        contentDescription = "Notetaker Logo",
                        modifier = Modifier.size(412.dp))
                }
                
                Column(
                    modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { navController.navigate("login_screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryColor),
                        modifier = Modifier.width(300.dp)) {
                        Text(
                            text = "Login",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    Button(
                        onClick = { navController.navigate("signup_screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.width(300.dp)) {
                        Text(
                            text = "Register",
                            color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {

    val window = rememberWindowSizeClass()
    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LandingPage()
        }
    }
}