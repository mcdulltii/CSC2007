package com.csc2007.notetaker.ui.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.csc2007.notetaker.ui.gallery.GallerySelect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun CameraPage(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    var cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri != EMPTY_IMAGE_URI) {
            Box(modifier = modifier) {
                Column {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Captured image"
                    )
                    val textLiveData = ocrImage(LocalContext.current, imageUri)
                    textLiveData.value?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it
                        )
                    }
                }
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
                        cameraSelector = cameraSelector,
                        onImageRotate = {
                            cameraSelector.value = if (cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                        },
                        onImageFile = { file ->
                            imageUri = file.toUri()
                        },
                        onImageSelect = {
                            showGallerySelect = true
                        }
                    )
                }
            }
        }
    }
}

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

private fun ocrImage(context: Context, uri: Uri): LiveData<String> {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromFilePath(context, uri)
    val textLiveData = MutableLiveData<String>()
    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            textLiveData.value = visionText.text
        }
        .addOnFailureListener { e ->
            Log.e("CameraPage", "Error recognizing text: $e")
            textLiveData.value = "Error: $e"
        }
    return textLiveData
}