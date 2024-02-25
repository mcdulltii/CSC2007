package com.csc2007.notetaker.ui.module.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Note

@Composable
fun ExpandableFloatingActionButton(isExpanded: Boolean, onExpand: () -> Unit, onClickToAddManually: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // The buttons that will appear when the FAB is expanded
        AnimatedVisibility(visible = isExpanded, enter = fadeIn() + expandVertically()) {
            Column {
                FloatingActionButton(
                    onClick = onClickToAddManually,
                    containerColor = Color(0xFF5590A2)
                ) {
                    Icon(Icons.Filled.Note, contentDescription = "Module", tint = Color.White)
                }
                Spacer(Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = {} ,
                    containerColor = Color(0xFF465E63)
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Camera", tint = Color.White)
                }
            }
        }

        // Main FAB
        FloatingActionButton(
            onClick = onExpand,
            shape = CircleShape,
            containerColor = Color(0xFF30628C)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}