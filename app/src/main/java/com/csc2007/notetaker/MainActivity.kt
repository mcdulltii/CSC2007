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
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModelFactory
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModelFactory
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

        val viewModelFactory = UserViewModelFactory(
            (application as NoteTakingApp).repository
        )

        val noteViewModelFactory =
            NoteViewModelFactory((application as NoteTakingApp).noteRepository)

        val moduleViewModelFactory = ModuleViewModelFactory(
            (application as NoteTakingApp).moduleRepository,
            applicationContext
        )

        setContent {
            MainApp(
                viewModelFactory = viewModelFactory,
                noteViewModelFactory = noteViewModelFactory,
                moduleViewModelFactory = moduleViewModelFactory
            )
        }
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun MainApp(
    viewModelFactory: UserViewModelFactory,
    noteViewModelFactory: NoteViewModelFactory,
    moduleViewModelFactory: ModuleViewModelFactory
) {
    val navController = rememberNavController()

    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavGraph(
                navController = navController,
                viewModelFactory = viewModelFactory,
                noteViewModelFactory = noteViewModelFactory,
                moduleViewModelFactory = moduleViewModelFactory
            )
        }
    }
}
