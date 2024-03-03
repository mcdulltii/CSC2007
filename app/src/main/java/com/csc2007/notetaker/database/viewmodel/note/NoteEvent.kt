package com.csc2007.notetaker.database.viewmodel.note

import com.csc2007.notetaker.database.Note

sealed interface NoteEvent {

    data class SortNotes(val sortType: SortType) : NoteEvent

    data class DeleteNote(val note: Note) : NoteEvent

    data class DeleteAllNotes(val moduleId: Int) : NoteEvent


    data class SaveNote(val title: String, val content: String, val moduleId: Int) : NoteEvent

    data class SearchNote(val query: String) : NoteEvent

}
