package com.csc2007.notetaker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.csc2007.notetaker.ui.util.Screens
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Glasses
import compose.icons.fontawesomeicons.solid.ShoePrints
import compose.icons.fontawesomeicons.solid.Tshirt


@Composable
fun TopNavBar(navController: NavController = rememberNavController(), route: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back Arrow",
            modifier = Modifier.clickable {
                if (route != null) {
                    navController.navigate(route)
                } else {
                    navController.popBackStack()
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(search: MutableState<String> = rememberSaveable { mutableStateOf("") },
                 isActive: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) })
{
    Row {
        SearchBar(
            query = search.value,
            onQueryChange = { search.value = it },
            onSearch = { isActive.value = false },
            active = isActive.value,
            onActiveChange = { isActive.value = isActive.value },
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
        ) {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarText(navController: NavController = rememberNavController(), title: String = "Add New Module", imageDisplay: Int? = null, navBack: Boolean = true, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = title,
            modifier = modifier,
            textAlign = TextAlign.Center)
        },
        navigationIcon = {
            if (navBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                }
            }
        },
        actions = {
            if (imageDisplay != null)
                Image(
                    painter = painterResource(id = imageDisplay),
                    contentDescription = "profile picture",
                    modifier = Modifier.size(60.dp).padding(end = 16.dp)
                )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),)
}

@Composable
fun AvatarBottomNavBar() {
    BottomAppBar {
        NavigationBarItem(
            onClick = { /*TODO*/ },
            label = {
                    Text(text = "Hat")
            },
            selected = false,
            icon = { Icon(Icons.Filled.Headset, contentDescription = "Hat Icon") }
        )

        NavigationBarItem(
            onClick = { /*TODO*/ },
            label = {
                Text(text = "Accessory")
            },
            selected = false,
            icon = { Icon(FontAwesomeIcons.Solid.Glasses, contentDescription = "Glasses Icon", modifier = Modifier.size(24.dp)) }
        )

        NavigationBarItem(
            onClick = { /*TODO*/ },
            label = {
                Text(text = "Shirt")
            },
            selected = false,
            icon = { Icon(FontAwesomeIcons.Solid.Tshirt, contentDescription = "Shirt Icon", modifier = Modifier.size(24.dp)) }
        )

        NavigationBarItem(
            onClick = { /*TODO*/ },
            label = {
                Text(text = "Pants")
            },
            selected = false,
            icon = { Icon(FontAwesomeIcons.Solid.Tshirt, contentDescription = "Shorts Icon", modifier = Modifier.size(24.dp)) }
        )

        NavigationBarItem(
            onClick = { /*TODO*/ },
            label = {
                Text(text = "Shoes")
            },
            selected = false,
            icon = { Icon(FontAwesomeIcons.Solid.ShoePrints, contentDescription = "Shoes Icon", modifier = Modifier.size(24.dp)) }
        )
    }
}

@Composable
fun BottomNavBar(
    navController: NavController = rememberNavController(),
    state: MutableState<Boolean> = mutableStateOf(false),
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        Screens.ModulesScreen, Screens.ChatScreen, Screens.PomodoroScreen, Screens.AvatarScreen, Screens.SettingsScreen
    )

    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(
                        text = screen.title!!,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                icon = {
                    screen.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp) // Adjust the size of the icon here
                        )
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Black, selectedTextColor = Color.Black
                ),
            )
        }
    }
}