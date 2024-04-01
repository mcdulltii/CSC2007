package com.csc2007.notetaker.ui.avatar

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.Item
import com.csc2007.notetaker.database.viewmodel.ItemViewModel
import com.csc2007.notetaker.database.viewmodel.OwnViewModel
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AvatarShopPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
    itemViewModel: ItemViewModel = viewModel(),
    ownViewModel: OwnViewModel = viewModel(),
    window: WindowSizeClass = rememberWindowSizeClass()
) {

    val itemPointsCost = 500

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.gift_box_2))

    var isPlaying by remember { mutableStateOf(false) }

    val progress by animateLottieCompositionAsState(composition = composition, isPlaying = isPlaying)

    var itemSnackBarState by remember { mutableStateOf(true) }
    var failedSnackBarState by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val items by itemViewModel.allItems.collectAsState()

    val randomItem = remember { mutableStateOf<Item?>(null) }

    // Get current logged in user's details
    val loggedInUser = userViewModel.loggedInUser.collectAsState().value
    val id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0 ) }
    val loggedInUserPoints = userViewModel.loggedInUserPoints.collectAsState().value
    Log.d("AvatarShopPage", "${loggedInUserPoints}")
    LaunchedEffect(key1 = progress) {
        if (progress == 0.01f) {
            isPlaying = true
        }

        if (progress == 1f) {
            isPlaying = false
        }
    }

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopNavBarText(navController = navController, title = "Avatar Shop")

            Box {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                )
            }

            Button(onClick = {
                if (loggedInUserPoints < itemPointsCost) {
                    coroutineScope.launch {
                        failedSnackBarState = true
                        delay(2000)
                        failedSnackBarState = false
                    }
                } else {
                    if (loggedInUser != null) {
                        userViewModel.updateUserPoints(points = (loggedInUserPoints - itemPointsCost), email = loggedInUser.email )
                        isPlaying = true
                        randomItem.value = selectRandomItem(items)
                        ownViewModel.insert(userId = id.value, itemId = randomItem.value!!.id)
                        coroutineScope.launch {
                            delay(1800)
                            itemSnackBarState = true

                            delay(3000)
                            itemSnackBarState = false
                        }
                    }
                }

            }) {
                Text(
                    text = "Purchase for 500 Coins")
            }

            Spacer(modifier = Modifier.weight(1f))

            if ((itemSnackBarState) and (randomItem.value != null)) {
                randomItem.value?.let { ShowItemSnackBar(it) }
            }

            if (failedSnackBarState) {
                ShowFailedSnackBar()
            }

            BottomNavBar(navController = navController)
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.78f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopNavBarText(navController = navController, title = "Avatar Shop")

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(1f)
                            .fillMaxWidth(0.5f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box() {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier.size(200.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (loggedInUserPoints < itemPointsCost) {
                                    coroutineScope.launch {
                                        failedSnackBarState = true
                                        delay(2000)
                                        failedSnackBarState = false
                                    }
                                } else {
                                    if (loggedInUser != null) {
                                        userViewModel.updateUserPoints(points = (loggedInUserPoints - itemPointsCost), email = loggedInUser.email )
                                        isPlaying = true
                                        randomItem.value = selectRandomItem(items)
                                        ownViewModel.insert(userId = id.value, itemId = randomItem.value!!.id)
                                        coroutineScope.launch {
                                            delay(1800)
                                            itemSnackBarState = true

                                            delay(3000)
                                            itemSnackBarState = false
                                        }
                                    }
                                }
                        }) {
                            Text(
                                text = "Purchase for 500 Coins")
                        }
                    }
                }
            }
            if ((itemSnackBarState) and (randomItem.value != null)) {
                randomItem.value?.let { ShowItemSnackBar(it) }
            }
            Row(modifier = Modifier.weight(1f)) {
                BottomNavBar(navController = navController)
            }
        }
    }
}

private fun selectRandomItem(items: List<Item>): Item {

    return items.random()
}

@Composable
private fun ShowItemSnackBar(item: Item) {

    val context = LocalContext.current

    val resID = context.resources.getIdentifier(
        item.image,
        "drawable",
        context.packageName
    )

    Snackbar(
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "You have obtained a ${item.rarity} ${item.name}!")

            Image(
                painter = painterResource(id = resID),
                contentDescription = "Item Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(50.dp))
        }
    }
}

@Composable
fun ShowFailedSnackBar() {
    Snackbar(
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Insufficient Coins!")
        }
    }
}

@Preview
@Composable
fun ShowItemSnackBarPreview() {
    val item = Item(id = 0, name = "Penguin Hood", "Hat", "Rare", "hat_1")
    ShowItemSnackBar(item)
}


@Preview(showBackground = true)
@Composable
fun AvatarShopPreview() {
    val window = rememberWindowSizeClass()
    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AvatarShopPage()
        }
    }
}