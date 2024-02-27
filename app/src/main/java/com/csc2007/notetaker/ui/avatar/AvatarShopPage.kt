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
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AvatarShopPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
    itemViewModel: ItemViewModel = viewModel(),
    ownViewModel: OwnViewModel = viewModel()
) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.gift_box_2))

    var isPlaying by remember { mutableStateOf(false) }

    val progress by animateLottieCompositionAsState(composition = composition, isPlaying = isPlaying)

    var itemSnackBarState by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    val items by itemViewModel.allItems.collectAsState()

    val randomItem = remember { mutableStateOf<Item?>(null) }

    // Get current logged in user's details
    val loggedInUser = userViewModel.loggedInUser.collectAsState().value
    val id = remember { mutableStateOf(if (loggedInUser !== null) loggedInUser.id else 0 ) }

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
        TopNavBarText(navController = navController, title = "Avatar Shop")
        
        Box() {
            LottieAnimation(
                composition = composition,
                progress = { progress }
            )
        }

        Button(onClick = {
            isPlaying = true
            randomItem.value = selectRandomItem(items)
            ownViewModel.insert(userId = id.value, itemId = randomItem.value!!.id)
            coroutineScope.launch {
                delay(1800)
                itemSnackBarState = true
            }

        }) {
            Text(text = "Purchase for 500 Coins")
        }

        Spacer(modifier = Modifier.weight(1f))

        if ((itemSnackBarState) and (randomItem.value != null)) {
            randomItem.value?.let { ShowItemSnackBar(it) }
        }

        BottomNavBar(navController = navController)
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
        modifier = Modifier.padding(8.dp)
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

@Preview
@Composable
fun ShowItemSnackBarPreview() {
    val item = Item(id = 0, name = "Penguin Hood", "Hat", "Rare", "hat_1")
    ShowItemSnackBar(item)
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