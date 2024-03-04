
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.HandScissors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAppBarWithBackButton(
    navController: NavController,
    title: String,
    onClickDelete: () -> Unit = {},
    onClickShare: () -> Unit = {},
    onClickSummary: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showSummarize by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title, color = Color.Black) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More Options", tint = Color.Black)
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Black
                        )
                    },
                    onClick = {
                        // Handle "Delete" click
                        showMenu = false
                        showDialog = true // Show confirmation dialog
                    }
                )
                DropdownMenuItem(
                    text = { Text("Share") },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = Color.Black
                        )
                    },
                    onClick = {
                        // Handle "Share" click
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Summarize") },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = FontAwesomeIcons.Solid.HandScissors,
                            contentDescription = "Summarize",
                            tint = Color.Black
                        )
                    },
                    onClick = {
                        // Handle "Summarize" click
                        showMenu = false
                        showSummarize = true
                    }
                )
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClickDelete()
                        showDialog = false
                    }
                ) {
                    Text("DELETE", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }

    if (showSummarize) {
        AlertDialog(
            onDismissRequest = { showSummarize = false },
            title = { Text(text = "Confirm Summarize") },
            text = { Text(text = "Are you sure you want to summarize this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClickSummary()
                        showSummarize = false
                    }
                ) {
                    Text("SUMMARIZE", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSummarize = false }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}
