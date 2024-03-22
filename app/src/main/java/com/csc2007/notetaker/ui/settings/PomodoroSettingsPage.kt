package com.csc2007.notetaker.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.PomodoroTimerViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.TopNavBarText
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass

@Composable
fun PomodoroSettingsPage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    pomodoroTimerViewModel: PomodoroTimerViewModel = PomodoroTimerViewModel(),
    window: WindowSizeClass = rememberWindowSizeClass()
) {

    var showPomodoroTimerDialog = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf("") }

    if (showPomodoroTimerDialog.value and selectedOption.value.isNotEmpty()) {
        PomodoroTimerDialog(showPomodoroTimerDialog, selectedOption.value, pomodoroTimerViewModel)
    }

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(modifier = modifier) {
            TopNavBarText(navController = navController, title = "Pomodoro Timer Settings")

            Column(modifier = Modifier.padding(16.dp)) {
                ListItem(
                    headlineContent = { Text(text = "Adjust Pomodoro Timer") },
                    trailingContent = {
                        Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                    },
                    leadingContent = {
                        Icon(Icons.Default.AccessTime, contentDescription = "Time Icon")
                    },
                    modifier = Modifier.clickable {
                        showPomodoroTimerDialog.value = true
                        selectedOption.value = "Pomodoro"
                    }
                )

                ListItem(
                    headlineContent = { Text(text = "Adjust Short Break Timer") },
                    trailingContent = {
                        Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                    },
                    leadingContent = {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = "Time Icon")
                    },
                    modifier = Modifier.clickable {
                        showPomodoroTimerDialog.value = true
                        selectedOption.value = "Short Break"
                    }
                )

                ListItem(
                    headlineContent = { Text(text = "Adjust Long Break Timer") },
                    trailingContent = {
                        Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                    },
                    leadingContent = {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = "Time Icon")
                    },
                    modifier = Modifier.clickable {
                        showPomodoroTimerDialog.value = true
                        selectedOption.value = "Long Break"
                    }
                )

                Divider(thickness = 1.dp, color = Color(0xFFCAC4D0))
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomNavBar(navController = navController)
        }
    } else {
        Column(modifier = modifier) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.78f)
                    .verticalScroll(rememberScrollState())
            ) {
                TopNavBarText(navController = navController, title = "Pomodoro Timer Settings")

                Column(modifier = Modifier.padding(16.dp)) {
                    ListItem(
                        headlineContent = { Text(text = "Adjust Pomodoro Timer") },
                        trailingContent = {
                            Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                        },
                        leadingContent = {
                            Icon(Icons.Default.AccessTime, contentDescription = "Time Icon")
                        },
                        modifier = Modifier.clickable {
                            showPomodoroTimerDialog.value = true
                            selectedOption.value = "Pomodoro"
                        }
                    )

                    ListItem(
                        headlineContent = { Text(text = "Adjust Short Break Timer") },
                        trailingContent = {
                            Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                        },
                        leadingContent = {
                            Icon(Icons.Default.HourglassEmpty, contentDescription = "Time Icon")
                        },
                        modifier = Modifier.clickable {
                            showPomodoroTimerDialog.value = true
                            selectedOption.value = "Short Break"
                        }
                    )

                    ListItem(
                        headlineContent = { Text(text = "Adjust Long Break Timer") },
                        trailingContent = {
                            Icon(Icons.Default.ArrowRight, contentDescription = "Right Arrow")
                        },
                        leadingContent = {
                            Icon(Icons.Default.HourglassEmpty, contentDescription = "Time Icon")
                        },
                        modifier = Modifier.clickable {
                            showPomodoroTimerDialog.value = true
                            selectedOption.value = "Long Break"
                        }
                    )

                    Divider(thickness = 1.dp, color = Color(0xFFCAC4D0))
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                BottomNavBar(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTimerDialog(
    showPomodoroTimerDialog: MutableState<Boolean> = mutableStateOf(false),
    title: String = "",
    pomodoroTimerViewModel: PomodoroTimerViewModel = PomodoroTimerViewModel()
) {

    var displayMinutes = remember { mutableStateOf("") }
    var displaySeconds = remember { mutableStateOf("") }

    val maxNumber = 60

    if (title == "Pomodoro") {
        displayMinutes.value = pomodoroTimerViewModel.pomodoroMinutes.collectAsState().value.toString()
        displaySeconds.value = pomodoroTimerViewModel.pomodoroSeconds.collectAsState().value.toString()
    } else if (title == "Short Break") {
        displayMinutes.value = pomodoroTimerViewModel.shortBreakMinutes.collectAsState().value.toString()
        displaySeconds.value = pomodoroTimerViewModel.shortBreakSeconds.collectAsState().value.toString()
    } else if (title == "Long Break") {
        displayMinutes.value = pomodoroTimerViewModel.longBreakMinutes.collectAsState().value.toString()
        displaySeconds.value = pomodoroTimerViewModel.longBreakSeconds.collectAsState().value.toString()
    }

    Dialog(onDismissRequest = { showPomodoroTimerDialog.value = false }) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Adjust $title Timer",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp)
                
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = displayMinutes.value,
                        onValueChange = {
                            if (it.isEmpty()) {
                                displayMinutes.value = 0.toString()
                            } else {
                                if (it.toInt() <= maxNumber ) {
                                    displayMinutes.value = it
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 57.sp,
                            textAlign = TextAlign.Center),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .widthIn(min = 120.dp, max = 120.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Text(
                        text = ":",
                        fontSize = 57.sp,
                        lineHeight = 64.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)

                    TextField(
                        value = displaySeconds.value,
                        onValueChange = {
                            if (it.isEmpty()) {
                                displaySeconds.value = 0.toString()
                            } else {
                                if (it.toInt() <= maxNumber ) {
                                    displaySeconds.value = it
                                }
                            }
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 57.sp,
                            textAlign = TextAlign.Center),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .widthIn(min = 120.dp, max = 120.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Minutes",
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        letterSpacing = 0.4.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp))

                    Text(
                        text = "Seconds",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        letterSpacing = 0.4.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Clock Icon")

                    Row {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.1.sp,
                            modifier = Modifier.clickable { showPomodoroTimerDialog.value = false }
                        )

                        Spacer(modifier = Modifier.width(40.dp))

                        Text(
                            text = "OK",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.1.sp,
                            modifier = Modifier.clickable {
                                pomodoroTimerViewModel.adjustTimer(displayMinutes.value.toInt(), displaySeconds.value.toInt(), title)
                                showPomodoroTimerDialog.value = false
                            })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PomodoroTimerDialogPreview() {

    val window = rememberWindowSizeClass()

    NoteTakerTheme(window) {
        PomodoroTimerDialog()
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroSettingsPagePreview() {
    val window = rememberWindowSizeClass()

    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PomodoroSettingsPage()
        }
    }
}