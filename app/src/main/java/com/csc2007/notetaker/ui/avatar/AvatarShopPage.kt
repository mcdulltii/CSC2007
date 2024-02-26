package com.csc2007.notetaker.ui.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.csc2007.notetaker.R
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AvatarShopPage(modifier: Modifier = Modifier, navController: NavController = rememberNavController()) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.gift_box_2))

    var isPlaying by remember { mutableStateOf(false) }

    val progress by animateLottieCompositionAsState(composition = composition, isPlaying = isPlaying)

    var itemSnackBarState by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = progress) {
        if (progress == 0.01f) {
            isPlaying = true
        }

        if (progress == 1f) {
            isPlaying = false
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopNavBarText(title = "Avatar Shop")
        
        Box() {
            LottieAnimation(
                composition = composition,
                progress = { progress }
            )
        }

        Button(onClick = {
            isPlaying = true
            coroutineScope.launch {
                delay(1800)
                itemSnackBarState = true
            }

        }) {
            Text(text = "Purchase for 500 Coins")
        }

        Spacer(modifier = Modifier.weight(1f))

        if (itemSnackBarState) {
            ShowItemSnackBar()
        }

        BottomNavBar(navController = navController)
    }
}

@Composable
private fun ShowItemSnackBar() {
    Snackbar(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "You have obtained a new pair of glasses!")

            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Item Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(50.dp))
        }
    }
}

@Preview
@Composable
fun ShowItemSnackBarPreview() {
    ShowItemSnackBar()
}


@Preview(showBackground = true)
@Composable
fun AvatarShopPreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AvatarShopPage()
        }
    }
}