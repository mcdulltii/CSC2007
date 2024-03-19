package com.csc2007.notetaker.ui.pomodoro

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.PomodoroTimerViewModel
import com.csc2007.notetaker.ui.AppTheme
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.Orientation
import com.csc2007.notetaker.ui.WindowSizeClass
import com.csc2007.notetaker.ui.rememberWindowSizeClass
import com.csc2007.notetaker.ui.util.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroPage(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    pomodoroTimerViewModel: PomodoroTimerViewModel = PomodoroTimerViewModel(),
    window: WindowSizeClass = rememberWindowSizeClass()
) {

    val context = LocalContext.current

    var displayMinutes = pomodoroTimerViewModel.displayMinutes.collectAsState()
    var displaySeconds = pomodoroTimerViewModel.displaySeconds.collectAsState()

    var timerState = pomodoroTimerViewModel.timerState.collectAsState()

    var selectedTimer = rememberSaveable { mutableStateOf("Pomodoro") }

    if (AppTheme.orientation == Orientation.Portrait) {
        Column(
            modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Pomodoro Timer",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp)

                            Icon(Icons.Default.Settings, contentDescription = "Pomodoro Settings", modifier = Modifier.clickable { navController.navigate(Screens.PomodoroSettingsScreen.route) })
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextField(
                                value = displayMinutes.value.padStart(2, '0'),
                                readOnly = true,
                                onValueChange = { },
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
                                    .widthIn(min = 116.dp, max = 116.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Text(
                                text = ":",
                                fontSize = 57.sp,
                                lineHeight = 64.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)

                            TextField(
                                value = displaySeconds.value.padStart(2, '0'),
                                readOnly = true,
                                onValueChange = { },
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
                                    .widthIn(min = 116.dp, max = 116.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        ElevatedButton(
                            onClick = {
                                if (!timerState.value) {
                                    pomodoroTimerViewModel.startTimer(context, selectedTimer.value)
                                } else {
                                    pomodoroTimerViewModel.pauseTimer()
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            if (!timerState.value) {
                                Text(text = "Start")
                            } else {
                                Text(text = "Pause")
                            }
                        }

                        ElevatedButton(
                            onClick = {
                                pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text(text = "Restart")
                        }

                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            selectedTimer.value = "Pomodoro"
                            pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                        },
                        enabled = selectedTimer.value !== "Pomodoro",
                        shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 0, bottomStartPercent = 50, bottomEndPercent = 0),
                        colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary, disabledContentColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.Transparent),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (selectedTimer.value == "Pomodoro") {
                            Icon(Icons.Default.Check, contentDescription = "Check Icon", modifier = Modifier.size(13.dp))

                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(text = "Pomodoro", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    OutlinedButton(
                        onClick = {
                            selectedTimer.value = "Short Break"
                            pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                        },
                        enabled = selectedTimer.value !== "Short Break",
                        shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 0, bottomStartPercent = 0, bottomEndPercent = 0),
                        colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary, disabledContentColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.Transparent),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (selectedTimer.value == "Short Break") {
                            Icon(Icons.Default.Check, contentDescription = "Check Icon", modifier = Modifier.size(13.dp))

                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(text = "Short Break", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    OutlinedButton(
                        onClick = {
                            selectedTimer.value = "Long Break"
                            pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                        },
                        enabled = selectedTimer.value !== "Long Break",
                        shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 50, bottomStartPercent = 0, bottomEndPercent = 50),
                        colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary, disabledContentColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.Transparent),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (selectedTimer.value == "Long Break") {
                            Icon(Icons.Default.Check, contentDescription = "Check Icon", modifier = Modifier.size(13.dp))

                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(text = "Long Break", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            BottomNavBar(navController = navController)
        }
    } else {

        Column(modifier = modifier) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.78f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Pomodoro Timer",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 16.sp,
                                        letterSpacing = 0.5.sp)

                                    Icon(Icons.Default.Settings, contentDescription = "Pomodoro Settings", modifier = Modifier.clickable { navController.navigate(Screens.PomodoroSettingsScreen.route) })
                                }

                                Spacer(modifier = Modifier.height(5.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextField(
                                        value = displayMinutes.value.padStart(2, '0'),
                                        readOnly = true,
                                        onValueChange = { },
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
                                            .widthIn(min = 116.dp, max = 116.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )

                                    Text(
                                        text = ":",
                                        fontSize = 57.sp,
                                        lineHeight = 64.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer)

                                    TextField(
                                        value = displaySeconds.value.padStart(2, '0'),
                                        readOnly = true,
                                        onValueChange = { },
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
                                            .widthIn(min = 116.dp, max = 116.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                ElevatedButton(
                                    onClick = {
                                        if (!timerState.value) {
                                            pomodoroTimerViewModel.startTimer(context, selectedTimer.value)
                                        } else {
                                            pomodoroTimerViewModel.pauseTimer()
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    if (!timerState.value) {
                                        Text(text = "Start")
                                    } else {
                                        Text(text = "Pause")
                                    }
                                }

                                ElevatedButton(
                                    onClick = {
                                        pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                                    },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    Text(text = "Restart")
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedButton(
                                onClick = {
                                    selectedTimer.value = "Pomodoro"
                                    pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                                },
                                enabled = selectedTimer.value !== "Pomodoro",
                                shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 0, bottomStartPercent = 50, bottomEndPercent = 0),
                                colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary, disabledContentColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.Transparent),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (selectedTimer.value == "Pomodoro") {
                                    Icon(Icons.Default.Check, contentDescription = "Check Icon", modifier = Modifier.size(13.dp))

                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(text = "Pomodoro", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }

                            OutlinedButton(
                                onClick = {
                                    selectedTimer.value = "Short Break"
                                    pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                                },
                                enabled = selectedTimer.value !== "Short Break",
                                shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 0, bottomStartPercent = 0, bottomEndPercent = 0),
                                colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary, disabledContentColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.Transparent),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (selectedTimer.value == "Short Break") {
                                    Icon(Icons.Default.Check, contentDescription = "Check Icon", modifier = Modifier.size(13.dp))

                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(text = "Short Break", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }

                            OutlinedButton(
                                onClick = {
                                    selectedTimer.value = "Long Break"
                                    pomodoroTimerViewModel.resetTimer(selectedTimer.value)
                                },
                                enabled = selectedTimer.value !== "Long Break",
                                shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 50, bottomStartPercent = 0, bottomEndPercent = 50),
                                colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary, disabledContentColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.Transparent),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (selectedTimer.value == "Long Break") {
                                    Icon(Icons.Default.Check, contentDescription = "Check Icon", modifier = Modifier.size(13.dp))

                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(text = "Long Break", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                BottomNavBar(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroPagePreview() {
    val window = rememberWindowSizeClass()
    NoteTakerTheme(window) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            PomodoroPage()
        }
    }
}
