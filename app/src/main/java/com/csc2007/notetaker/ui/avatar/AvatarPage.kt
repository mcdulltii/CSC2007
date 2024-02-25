package com.csc2007.notetaker.ui.avatar

import android.graphics.Insets.add
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.csc2007.notetaker.R
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.util.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarPage(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
    ) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components{
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column(
            modifier = Modifier
                .height(500.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.base_avatar),
                    contentDescription = "Avatar Image",
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center))

                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    FloatingActionButton(onClick = { navController.navigate(Screens.AvatarEditScreen.route) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Icon")
                    }
                }
            }

            Row(
                modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)
            ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = "Money Icon")

                Text(text = "30,000")
            }

            ElevatedButton(onClick = {
                navController.navigate(Screens.AvatarShopScreen.route)
            }) {
                Text(text = "Shop")
            }
        }



        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AvatarPage()
        }
    }
}