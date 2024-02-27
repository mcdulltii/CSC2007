package com.csc2007.notetaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.database.NoteTakingApp
import com.csc2007.notetaker.database.viewmodel.AvatarViewModelFactory
import com.csc2007.notetaker.database.viewmodel.ItemViewModelFactory
import com.csc2007.notetaker.database.viewmodel.OwnViewModelFactory
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.util.NavGraph
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModelFactory = UserViewModelFactory(
            (application as NoteTakingApp).userRepository
        )

        val itemViewModelFactory = ItemViewModelFactory(
            (application as NoteTakingApp).itemRepository
        )

        val ownViewModelFactory = OwnViewModelFactory(
            (application as NoteTakingApp).ownRepository
        )

        val avatarViewModelFactory = AvatarViewModelFactory(
            (application as NoteTakingApp).avatarRepository
        )

        setContent {
            MainApp(userViewModelFactory = userViewModelFactory, itemViewModelFactory = itemViewModelFactory, ownViewModelFactory = ownViewModelFactory, avatarViewModelFactory = avatarViewModelFactory)
        }
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun MainApp(
    userViewModelFactory: UserViewModelFactory,
    itemViewModelFactory: ItemViewModelFactory,
    ownViewModelFactory: OwnViewModelFactory,
    avatarViewModelFactory: AvatarViewModelFactory
) {
    val navController = rememberNavController()

    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavGraph(navController = navController, userViewModelFactory = userViewModelFactory, itemViewModelFactory = itemViewModelFactory, ownViewModelFactory = ownViewModelFactory, avatarViewModelFactory = avatarViewModelFactory)
        }
    }
}
