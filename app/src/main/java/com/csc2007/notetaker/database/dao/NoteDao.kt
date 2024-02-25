package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.csc2007.notetaker.database.entity.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNotesOrderedByASCTitle(): Flow<List<Note>>

    @Query("SELECT * from note ORDER BY title DESC")
    fun getNotesOrderedByDESCTitle(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateAdded ASC")
    fun getNotesOrderedByASCDate(): Flow<List<Note>>

    @Query("SELECT * from note ORDER BY dateAdded DESC")
    fun getNotesOrderedByDESCDate(): Flow<List<Note>>

//    @Query("SELECT DISTINCT module FROM note")
//    fun getUniqueModules(): Flow<List<String>>
}