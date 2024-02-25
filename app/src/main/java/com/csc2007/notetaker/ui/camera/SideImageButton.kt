package com.csc2007.notetaker.ui.camera

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times

@Composable
fun SideImageButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = FontAwesomeIcons.Solid.Times,
    desc: String = "",
    onClick: () -> Unit = { },
) {
    OutlinedButton(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "${desc} Icon",
            modifier = Modifier
                .size(35.dp)
                .padding(vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSideImageButton() {
    Scaffold(
        modifier = Modifier
            .size(125.dp)
            .wrapContentSize()
    ) { innerPadding ->
        SideImageButton(
            modifier = Modifier
                .padding(innerPadding)
                .size(100.dp)
        )
    }
}
