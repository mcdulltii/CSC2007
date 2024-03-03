package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import com.csc2007.notetaker.database.dao.NoteDao
import com.csc2007.notetaker.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NotesRepository(private val noteDao: NoteDao) {

    val notesOrderedByASCTitle: Flow<List<Note>> = noteDao.getNotesOrderedByASCTitle()
    val notesOrderedByDESCTitle: Flow<List<Note>> = noteDao.getNotesOrderedByDESCTitle()

    val notesOrderedByASCDate: Flow<List<Note>> = noteDao.getNotesOrderedByASCDate()
    val notesOrderedByDESCDate: Flow<List<Note>> = noteDao.getNotesOrderedByDESCDate()

    val notes: Flow<List<Note>> = noteDao.getAllNotes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun upsertNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.upsertNote(note)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteALlNote(moduleId: Int) {
        withContext(Dispatchers.IO) {
            noteDao.deleteAllNotes(moduleId)
        }
    }

}