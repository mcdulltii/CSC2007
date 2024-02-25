package com.csc2007.notetaker.ui.modules

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.R
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.TopNavBarText

@Composable
fun AddModulePage(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {

    var moduleName = remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        TopNavBarText(navController = navController, title = "Add New Module")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Module Image",
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Start))

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.notetaker_logo),
                contentDescription = "Module Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(188.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.outline))

            Spacer(modifier = Modifier.height(15.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Upload Image")
            }

            OutlinedTextField(
                value = moduleName.value,
                onValueChange = { moduleName.value = it },
                label = { Text(text = "Module Name") },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(55.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.widthIn(min = 182.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Add Icon")
                Text(text = "Create Module")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.widthIn(min = 182.dp)) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSecondary)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar()
    }
}

@Preview(showBackground = true)
@Composable
fun AddModulePagePreview() {
    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddModulePage()
        }
    }
}