package com.csc2007.notetaker.ui.module.pages

import ModuleAppBarWithSortTypes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.database.viewmodel.module.ModuleEvent
import com.csc2007.notetaker.database.viewmodel.module.ModuleState
import com.csc2007.notetaker.ui.BottomNavBar
import com.csc2007.notetaker.ui.module.components.ModuleItem
import com.csc2007.notetaker.ui.module.components.ModuleSearchBar
import com.csc2007.notetaker.ui.util.Screens


@Composable
fun ModulesPage(
    navController: NavHostController = rememberNavController(),
    state: ModuleState,
    onEvent: (ModuleEvent) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.AddModuleScreen.route)} ,
                containerColor = Color(0xFF465E63)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        topBar = {
            Column {
                ModuleSearchBar(state = state, onEvent = onEvent)
                ModuleAppBarWithSortTypes(onEvent = onEvent)
            }
        },
        bottomBar = { BottomNavBar(navController = navController) }

    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.modules.isNotEmpty()) {
                    items(state.modules.size) { index ->
                        ModuleItem(
                            state = state,
                            onEvent = onEvent,
                            navController = navController,
                            index = index
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "No modules yet.",
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }

        }
    }
}
