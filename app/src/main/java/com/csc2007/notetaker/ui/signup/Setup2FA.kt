package com.csc2007.notetaker.ui.signup

import androidx.compose.foundation.layout.Arrangement
import org.apache.commons.codec.binary.Base32
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass
import com.csc2007.notetaker.ui.util.Screens
import com.lightspark.composeqr.QrCodeView
import dev.turingcomplete.kotlinonetimepassword.OtpAuthUriBuilder


@Composable
fun Setup2FA(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
    window: WindowSizeClass = rememberWindowSizeClass()) {

    val secret: ByteArray? = userViewModel.registeredUserSecret.collectAsState().value
    val base32EncodedSecret = Base32().encodeAsString(secret)
    val url = OtpAuthUriBuilder.forTotp(Base32().encode(secret))
        .label(issuer = "Notetaker", accountName = "OTP")
        .issuer("Company")
        .digits(6)
        .buildToString()

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(modifier = modifier) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Scan the QR Code using Google Authenticator",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                    )
                Spacer(modifier = Modifier.height(20.dp))

                QrCodeView(
                    data = url,
                    modifier = Modifier.size(300.dp))

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "or enter the secret key in ur Google Authenticator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "$base32EncodedSecret")

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { navController.navigate(Screens.LoginScreen.route) }) {
                    Text(text = "Done")
                }
            }
        }
    } else {
        Column(modifier = modifier) {

            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Scan the QR Code using Google Authenticator",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    QrCodeView(
                        data = url,
                        modifier = Modifier.size(300.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "or enter the secret key in ur Google Authenticator",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center)

                    Text(text = "$base32EncodedSecret")

                    Button(onClick = { navController.navigate(Screens.LoginScreen.route) }) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun Setup2FAPreview() {
    val window = rememberWindowSizeClass()

    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Setup2FA()
        }
    }
}