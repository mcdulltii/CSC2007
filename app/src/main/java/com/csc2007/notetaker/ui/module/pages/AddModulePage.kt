package com.csc2007.notetaker.ui.module.pages

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.csc2007.notetaker.R
import com.csc2007.notetaker.database.viewmodel.module.ModuleEvent
import com.csc2007.notetaker.database.viewmodel.module.ModuleState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddModulePage(
    navController: NavController,
    onEvent: (ModuleEvent) -> Unit,
    state: ModuleState
) {

    var moduleTitle = remember { mutableStateOf("") }

    val imageUriStr = rememberSaveable {
        mutableStateOf("")
    }

    val context: Context = LocalContext.current

    val defaultImageUri: Uri = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.resources.getResourcePackageName(R.drawable.notetaker_logo) +
                '/' + context.resources.getResourceTypeName(R.drawable.notetaker_logo) +
                '/' + context.resources.getResourceEntryName(R.drawable.notetaker_logo)
    )


    val painter =
        rememberAsyncImagePainter(model = imageUriStr.value)


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUriStr.value = it.toString() }
        }

    fun onSaveModuleClicked() {
        val imageUri =
            if (imageUriStr.value.isNotEmpty()) Uri.parse(imageUriStr.value) else defaultImageUri

        imageUri?.let { ModuleEvent.SaveModule(moduleTitle.value, it) }
            ?.let { onEvent(it) }

        navController.navigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add New Module",
                        style = TextStyle(fontWeight = FontWeight.SemiBold),
                        fontSize = 20.sp
                    )
                }, // The title of your page
                navigationIcon = {
                    // Back button
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, // Using a Material icon for back arrow
                            contentDescription = "Back"
                        )
                    }
                }
            )

        },

        ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Text("Module Image")

            }
            Image(
                painter = painter,
                contentDescription = "Module Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(188.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Upload Image")
            }

            OutlinedTextField(
                value = moduleTitle.value,
                onValueChange = { moduleTitle.value = it },
                label = { Text(text = "Module Name") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(55.dp))

            Button(
                onClick = { onSaveModuleClicked() },
                modifier = Modifier.widthIn(min = 182.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Icon")
                Text(text = "Create Module")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.widthIn(min = 182.dp)
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

        }
    }

}

