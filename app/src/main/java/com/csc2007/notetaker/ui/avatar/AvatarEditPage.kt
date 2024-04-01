package com.csc2007.notetaker.ui.avatar

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.csc2007.notetaker.database.AvatarItem
import com.csc2007.notetaker.database.viewmodel.AvatarViewModel
import com.csc2007.notetaker.database.viewmodel.ItemViewModel
import com.csc2007.notetaker.database.viewmodel.OwnViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.AvatarBottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass

@Composable
fun AvatarEditPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
    itemViewModel: ItemViewModel = viewModel(),
    ownViewModel: OwnViewModel = viewModel(),
    avatarViewModel: AvatarViewModel = viewModel(),
    window: WindowSizeClass = rememberWindowSizeClass()
) {

    // Get current logged in user's details
    val loggedInUser = userViewModel.loggedInUser.collectAsState().value
    val id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0) }

    val type = remember { mutableStateOf("Hat") }

    // Get current logged in user's items
    ownViewModel.getOwnedItems(userId = id.value, type = type.value)
    val ownedItems by ownViewModel.ownedItems.collectAsState()

    // Get current logged in user's avatar
    avatarViewModel.getUserAvatar(userId = id.value)
    avatarViewModel.getUserAvatarImage()
    val avatarImageString = avatarViewModel.avatarImageString.collectAsState()

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val avatarResId = context.resources.getIdentifier(
        avatarImageString.value,
        "drawable",
        context.packageName
    )
    Log.d("AvatarEditPage", "${avatarImageString}")

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(modifier = modifier) {
            TopNavBarText(navController = navController, title = "Edit Avatar")

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = avatarResId).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = "Avatar Image",
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    avatarViewModel.unEquipItem(userId = id.value, itemType = type.value)
                }) {
                    Text(text = "Unequip")
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (ownedItems != null) {
                        items(ownedItems!!) { item ->

                            val resId = context.resources.getIdentifier(
                                item.image,
                                "drawable",
                                context.packageName
                            )

                            Box(
                                modifier = Modifier.aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        avatarViewModel.equipItem(userId = id.value, item.itemId, item.type)
                                    },
                                ) {
                                    Image(
                                        painter = painterResource(id = resId),
                                        contentDescription = "Avatar Image",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .size(50.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AvatarBottomNavBar(type = type)
        }
    } else {

        Column(modifier = modifier) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.78f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopNavBarText(navController = navController, title = "Edit Avatar")

                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context).data(data = avatarResId).apply(block = {
                                    size(Size.ORIGINAL)
                                }).build(), imageLoader = imageLoader
                            ),
                            contentDescription = "Avatar Image",
                            modifier = Modifier
                                .size(150.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        Button(onClick = {
                            avatarViewModel.unEquipItem(userId = id.value, itemType = type.value)
                        }) {
                            Text(text = "Unequip")
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                            .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (ownedItems != null) {
                                items(ownedItems!!) { item ->

                                    val resId = context.resources.getIdentifier(
                                        item.image,
                                        "drawable",
                                        context.packageName
                                    )

                                    Box(
                                        modifier = Modifier.aspectRatio(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        FloatingActionButton(
                                            onClick = {
                                                avatarViewModel.equipItem(userId = id.value, item.itemId, item.type)
                                            },
                                        ) {
                                            Image(
                                                painter = painterResource(id = resId),
                                                contentDescription = "Avatar Image",
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .size(50.dp)
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                AvatarBottomNavBar(type = type)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AvatarEditPagePreview() {
    val window = rememberWindowSizeClass()
    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AvatarEditPage()
        }
    }
}