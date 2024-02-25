package com.csc2007.notetaker.ui.modules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopSearchBar
import com.csc2007.notetaker.ui.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesPage(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.DarkGray)
//            .wrapContentSize(Alignment.Center)
//    ) {
//        Text(
//            text = "Module Screen",
//            style = MaterialTheme.typography.titleLarge,
//            color = Color.White,
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            textAlign = TextAlign.Center,
//        )
//    }

    var titleFilterSelected = remember { mutableStateOf(false) }
    var dateFilterSelected = remember { mutableStateOf(false) }
    var ascendingFilterSelected = remember { mutableStateOf(false) }
    var descendingFilterSelected = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopSearchBar()

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = titleFilterSelected.value,
                onClick = { titleFilterSelected.value = !titleFilterSelected.value },
                label = { Text(text = "Title") }
            )

            FilterChip(
                selected = dateFilterSelected.value,
                onClick = { dateFilterSelected.value = !dateFilterSelected.value },
                label = { Text(text = "Date") }
            )

            FilterChip(
                selected = ascendingFilterSelected.value,
                onClick = { ascendingFilterSelected.value = !ascendingFilterSelected.value },
                label = { Text(text = "Ascending") }
            )

            FilterChip(
                selected = descendingFilterSelected.value,
                onClick = { descendingFilterSelected.value = !descendingFilterSelected.value },
                label = { Text(text = "Descending") }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Module("Mobile Application Development", "07 Feb 2024")

        Spacer(modifier.weight(1f))

        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.End).padding(16.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Add Module")
        }

        BottomNavBar(navController = navController)
    }
}

@Composable
fun Module(moduleName: String, lastEdited: String) {
    Card(
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.Transparent)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = colors.primaryColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.widthIn(max = 192.dp)
            ) {
                Text(
                    text = moduleName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last Edited: $lastEdited",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.25.sp)
            }

            Image(
                painter = painterResource(id = R.drawable.notetaker_logo),
                contentDescription = "Module Image",
                modifier = Modifier.size(width = 80.dp, height = 90.dp),
                contentScale = ContentScale.FillBounds)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModulesPagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ModulesPage()
        }
    }
}