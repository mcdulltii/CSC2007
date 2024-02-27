package com.csc2007.notetaker.ui.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.csc2007.notetaker.ui.gallery.GallerySelect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Captured image"
                    )
                    val recognizedText = ocrImage(LocalContext.current, imageUri)
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        value = recognizedText.value ?: "",
                        onValueChange = { /* Handle text changes if needed */ },
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        readOnly = true,
                        maxLines = Int.MAX_VALUE
                    )
                }
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = {
                        imageUri = EMPTY_IMAGE_URI
                    }
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove image")
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

@Composable
private fun ocrImage(context: Context, uri: Uri): MutableState<String?> {
    val recognizedText = remember { mutableStateOf<String?>(null) }

    val image = InputImage.fromFilePath(context, uri)
    val recognizer: TextRecognizer = TextRecognition.getClient()
    recognizer.process(image)
        .addOnSuccessListener { texts ->
            recognizedText.value = processTextRecognitionResult(texts)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            recognizedText.value = "Text recognition failed"
        }

    return recognizedText
}

private fun processTextRecognitionResult(texts: Text): String? {
    val blocks: List<Text.TextBlock> = texts.textBlocks
    if (blocks.size == 0) {
        return "No text recognized"
    }

    val recognizedText = StringBuilder()
    for (i in blocks.indices) {
        val lines: List<Text.Line> = blocks[i].getLines()
        for (j in lines.indices) {
            val elements: List<Text.Element> = lines[j].getElements()
            for (k in elements.indices) {
                recognizedText.append(elements[k].text).append(" ")
            }
        }
    }
    return recognizedText.toString().trim()
}