package com.csc2007.notetaker

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.csc2007.notetaker.camera.CameraCapture
import com.csc2007.notetaker.database.NoteTakingApp
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.gallery.GallerySelect
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

        setContent {

            MainApp(viewModelFactory = viewModelFactory)
//            ComposePhotoIntegrationTheme {
                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
////                    MainContent(Modifier.fillMaxSize())
//                }

//                val navController: NavHostController = rememberNavController()
//                val bottomBarHeight = 56.dp
//                val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
//
//                var buttonsVisible = remember { mutableStateOf(true) }
//
//
//
//                Scaffold(
//                    bottomBar = {
//                        BottomBar(
//                            navController = navController,
//                            state = buttonsVisible,
//                            modifier = Modifier
//                                .height(bottomBarHeight)
//                                .offset {
//                                    IntOffset(
//                                        x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()
//                                    )
//                                }
//                        )
//                    }) { paddingValues ->
//                    Box(
//                        modifier = Modifier.padding(paddingValues)
//                    ) {
//                        NavGraph(navController = navController)
//                    }
//                }

        }
    }
}

@Composable
fun MainApp(
    viewModelFactory: UserViewModelFactory
) {
    val navController = rememberNavController()

    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavGraph(navController = navController, viewModelFactory = viewModelFactory)
        }
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    if (imageUri != EMPTY_IMAGE_URI) {
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(imageUri),
                contentDescription = "Captured image"
            )
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                }
            ) {
                Text("Remove image")
            }
        }
    } else {
        var showGallerySelect by remember { mutableStateOf(false) }
        if (showGallerySelect) {
            GallerySelect(
                modifier = modifier,
                onImageUri = { uri ->
                    showGallerySelect = false
                    imageUri = uri
                }
            )
        } else {
            Box(modifier = modifier) {
                CameraCapture(
                    modifier = modifier,
                    onImageFile = { file ->
                        imageUri = file.toUri()
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(4.dp),
                    onClick = {
                        showGallerySelect = true
                    }
                ) {
                    Text("Select from Gallery")
                }
            }
        }
    }
}

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")
