package com.csc2007.notetaker.ui.avatar

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.ItemViewModel
import com.csc2007.notetaker.database.viewmodel.OwnViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.AvatarBottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText

@Composable
fun AvatarEditPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
    itemViewModel: ItemViewModel = viewModel(),
    ownViewModel: OwnViewModel = viewModel()
) {

    // Get current logged in user's details
    val loggedInUser = userViewModel.loggedInUser.collectAsState().value
    val id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0) }

    // Get current logged in user's items
    ownViewModel.getOwnedItems(userId = id.value)
    val ownedItems by ownViewModel.ownedItems.collectAsState()

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components{
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


//    val hats = listOf(R.drawable.hat_1, R.drawable.hat_1, R.drawable.hat_1, R.drawable.hat_1)

    Column(modifier = modifier){
        TopNavBarText(navController = navController, title = "Edit Avatar")

        Image(
            painter = rememberAsyncImagePainter(ImageRequest.Builder(context).data(data = R.drawable.base_avatar).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader),
            contentDescription = "Avatar Image",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.weight(1f))

        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .height(400.dp)
                    .clip(shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
                horizontalArrangement = Arrangement.Center) {
                if (ownedItems !== null) {
                    items(ownedItems!!) {
                        item ->

                        val resID = context.resources.getIdentifier(
                            item.image,
                            "drawable",
                            context.packageName
                        )

                        Box(
                            modifier = Modifier.aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            FloatingActionButton(
                                onClick = { /*TODO*/ },) {
                                Image(
                                    painter = painterResource(id = resID),
                                    contentDescription = "Avatar Image",
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(50.dp)
                                        .align(Alignment.Center))
                            }
                        }
                    }
                }

            }

            AvatarBottomNavBar()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AvatarEditPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AvatarEditPage()
        }
    }
}