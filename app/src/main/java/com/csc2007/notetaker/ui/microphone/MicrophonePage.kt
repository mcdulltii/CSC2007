package com.csc2007.notetaker.ui.microphone

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState

@Composable
fun MicrophonePage(
    navController: NavHostController = rememberNavController(),
    voiceToTextParser: VoiceToTextParser,
    onEvent: (NoteEvent) -> Unit = {},
    noteState: NoteState
) {
    val context = LocalContext.current
    val microphonePermission = Manifest.permission.RECORD_AUDIO
    var isMicrophonePermissionGranted by remember { mutableStateOf(false) }
    val state by voiceToTextParser.state.collectAsState()

    val moduleId = navController.currentBackStackEntry?.arguments?.getInt("moduleId") ?: -1

    val microphoneLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isMicrophonePermissionGranted = isGranted
        }
    }

    // Perform permission check when the composable is initially composed
    LaunchedEffect(key1 = microphonePermission) {
        isMicrophonePermissionGranted = ContextCompat.checkSelfPermission(
            context,
            microphonePermission
        ) == PackageManager.PERMISSION_GRANTED

        if (!isMicrophonePermissionGranted) {
            microphoneLauncher.launch(microphonePermission)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (!isMicrophonePermissionGranted) {
                    microphoneLauncher.launch(microphonePermission)
                } else {
                    if (state.isSpeaking) {
                        voiceToTextParser.stopListening()

                        noteState.title.value = "Default Title"
                        noteState.content.value = state.spokenText.toString().ifEmpty { "No text recognized" }
                        noteState.moduleId.value = moduleId

                        onEvent(
                            NoteEvent.SaveNote(
                                title = noteState.title.value,
                                content =  noteState.content.value,
                                moduleId = noteState.moduleId.value,
                            )
                        )
                        navController.popBackStack()
                    } else {
                        voiceToTextParser.startListening()
                    }
                }
            }) {
                AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
                    if (isSpeaking) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    } else {
                        Icon(imageVector = Icons.Rounded.Mic, contentDescription = null)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
                if (isSpeaking) {
                    Text(text = "Speaking ...")
                } else {
                    Text(text = state.spokenText.toString().ifEmpty { "Click on mic to record" })
                }
            }
        }
    }
}
