package com.csc2007.notetaker.ui.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroPage(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    var selectedMinute = remember { mutableStateOf("20") }
    var selectedSecond = remember { mutableStateOf("00") }

    Column(
        modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f),
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

                        Icon(Icons.Default.Settings, contentDescription = "Pomodoro Settings")
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = selectedMinute.value,
                            onValueChange = { selectedMinute.value = it },
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
                            value = selectedSecond.value,
                            onValueChange = { selectedSecond.value = it },
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
                        onClick = { /*TODO*/ },
                        modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text = "Start")
                    }

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 0, bottomStartPercent = 50, bottomEndPercent = 0)
                ) {
                    Text(text = "Pomodoro")
                }

                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 0, bottomStartPercent = 0, bottomEndPercent = 0)
                ) {
                    Text(text = "Break")
                }

                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 50, bottomStartPercent = 0, bottomEndPercent = 50)
                ) {
                    Text(text = "Long Break")
                }
            }
        }

        BottomNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            PomodoroPage()
        }
    }
}
