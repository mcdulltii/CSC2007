package com.csc2007.notetaker.ui.camera

import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState
import com.csc2007.notetaker.ui.gallery.GallerySelect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.pspdfkit.document.PdfDocumentLoader
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun CameraPage(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    onEvent: (NoteEvent) -> Unit = {},
    state: NoteState
) {
    var imageUri by rememberSaveable { mutableStateOf(EMPTY_IMAGE_URI) }
    var cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    val moduleId = navController.currentBackStackEntry?.arguments?.getInt("moduleId") ?: -1

    var recognizedText = rememberSaveable { mutableStateOf<String?>(null) }
    val titleText = rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val orientation = LocalConfiguration.current.orientation
    val context = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri != EMPTY_IMAGE_URI) {
            if (isPdf(contentResolver, imageUri)) {
                try {
                    val document = PdfDocumentLoader.openDocument(context, imageUri)
                    val pageIndexes: Set<Int> = (0 until document.pageCount).toSet()
                    val allPagesText = StringBuilder()

                    // Iterate through all pages of the document
                    for (pageIndex in pageIndexes) {
                        // Extract text from the current page
                        val pageText = document.getPageText(pageIndex)
                        allPagesText.append(pageText)
                        allPagesText.append("\n\n")
                    }
                    recognizedText.value = allPagesText.toString()
                } catch (e: Exception) {
                    recognizedText.value = "Error: $e"
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Title", fontSize = 25.sp)
                            TextField(modifier = Modifier
                                .fillMaxWidth(), value = titleText.value ?: "",
                                onValueChange = { titleText.value = it },
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                maxLines = 1,
                                placeholder = { Text("Enter a title...") }
                            )
                        }

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            value = recognizedText.value ?: "",
                            onValueChange = { /* Handle text changes if needed */ },
                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                            readOnly = true,
                            maxLines = Int.MAX_VALUE
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Column {
                            FloatingActionButton(
                                modifier = Modifier
                                    .padding(16.dp),
                                onClick = {
                                    if (recognizedText.value != null) {
                                        state.title.value =
                                            titleText.value.ifEmpty { "Default Title" }
                                        state.content.value = recognizedText.value!!
                                        state.moduleId.value = moduleId

                                        onEvent(
                                            NoteEvent.SaveNote(
                                                title = state.title.value,
                                                content = state.content.value,
                                                moduleId = state.moduleId.value,
                                            )
                                        )
                                        navController.popBackStack()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add or Submit"
                                )
                            }
                            FloatingActionButton(
                                modifier = Modifier
                                    .padding(16.dp),
                                onClick = {
                                    // This is your existing action to "Remove image"
                                    imageUri = EMPTY_IMAGE_URI
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove image"
                                )
                            }
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    scrollState.scrollTo(0)
                }
            } else {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally),
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Captured image"
                            )
                            recognizedText = ocrImage(LocalContext.current, imageUri)

                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Title", fontSize = 25.sp)
                                TextField(modifier = Modifier
                                    .fillMaxWidth(), value = titleText.value ?: "",
                                    onValueChange = { titleText.value = it },
                                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                    maxLines = 1,
                                    placeholder = { Text("Enter a title...") }
                                )
                            }

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = recognizedText.value ?: "",
                                onValueChange = { /* Handle text changes if needed */ },
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                readOnly = true,
                                maxLines = Int.MAX_VALUE
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Column {
                                FloatingActionButton(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    onClick = {
                                        if (recognizedText.value != null) {
                                            state.title.value =
                                                titleText.value.ifEmpty { "Default Title" }
                                            state.content.value = recognizedText.value!!
                                            state.moduleId.value = moduleId

                                            onEvent(
                                                NoteEvent.SaveNote(
                                                    title = state.title.value,
                                                    content = state.content.value,
                                                    moduleId = state.moduleId.value,
                                                )
                                            )
                                            navController.popBackStack()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add or Submit"
                                    )
                                }
                                FloatingActionButton(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    onClick = {
                                        // This is your existing action to "Remove image"
                                        imageUri = EMPTY_IMAGE_URI
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove image"
                                    )
                                }
                            }
                        }
                    }
                    LaunchedEffect(Unit) {
                        scrollState.scrollTo(scrollState.maxValue)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth(0.5F)
                                    .aspectRatio(1f),
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Captured image"
                            )
                            recognizedText = ocrImage(LocalContext.current, imageUri)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.7F)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Text("Title", fontSize = 25.sp)
                                    TextField(modifier = Modifier
                                        .fillMaxWidth(), value = titleText.value ?: "",
                                        onValueChange = { titleText.value = it },
                                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                        maxLines = 1,
                                        placeholder = { Text("Enter a title...") }
                                    )
                                }

                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    value = recognizedText.value ?: "",
                                    onValueChange = { /* Handle text changes if needed */ },
                                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                    readOnly = true,
                                    maxLines = Int.MAX_VALUE
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Column {
                                FloatingActionButton(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    onClick = {
                                        if (recognizedText.value != null) {
                                            state.title.value =
                                                titleText.value.ifEmpty { "Default Title" }
                                            state.content.value = recognizedText.value!!
                                            state.moduleId.value = moduleId

                                            onEvent(
                                                NoteEvent.SaveNote(
                                                    title = state.title.value,
                                                    content = state.content.value,
                                                    moduleId = state.moduleId.value,
                                                )
                                            )
                                            navController.popBackStack()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add or Submit"
                                    )
                                }
                                FloatingActionButton(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    onClick = {
                                        // This is your existing action to "Remove image"
                                        imageUri = EMPTY_IMAGE_URI
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove image"
                                    )
                                }
                            }
                        }
                    }
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
                            cameraSelector.value =
                                if (cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) {
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

    try {
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
    } catch (e: Exception) {
        recognizedText.value = "Error: $e"
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

fun isPdf(contentResolver: ContentResolver, uri: Uri): Boolean {
    // Get the MIME type of the file
    val mimeType = contentResolver.getType(uri)
    return if (mimeType != null) {
        // Check if the MIME type indicates a PDF
        mimeType == "application/pdf"
    } else {
        // If MIME type is null, check the file extension
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        fileExtension.equals("pdf", ignoreCase = true)
    }
}