package com.csc2007.notetaker.database.viewmodel.note

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.csc2007.notetaker.database.entity.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val content: MutableState<String> = mutableStateOf(""),
    val sortType: SortType = SortType.ASC_TITLE,
    var moduleId: MutableState<Int> = mutableStateOf(0),
    val searchQuery: String = "",
)
enum class SortType {
    ASC_TITLE,
    DESC_TITLE,
    ASC_DATE_ADDED,
    DESC_DATE_ADDED
}