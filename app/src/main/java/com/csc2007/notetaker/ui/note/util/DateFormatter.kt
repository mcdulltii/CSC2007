package com.csc2007.notetaker.ui.note.util


import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(timestamp)
}
