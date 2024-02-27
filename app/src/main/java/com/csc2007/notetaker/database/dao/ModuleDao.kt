package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.csc2007.notetaker.database.Module
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleDao {

    @Upsert
    suspend fun upsertModule(module: Module)

    @Delete
    suspend fun deleteModule(module: Module)

    @Query("SELECT * FROM module_table")
    fun getAllNotes(): Flow<List<Module>>

    @Query("SELECT * FROM module_table ORDER BY title ASC")
    fun getNotesOrderedByASCTitle(): Flow<List<Module>>

    @Query("SELECT * from module_table ORDER BY title DESC")
    fun getNotesOrderedByDESCTitle(): Flow<List<Module>>

    @Query("SELECT * FROM module_table ORDER BY dateCreated ASC")
    fun getNotesOrderedByASCDate(): Flow<List<Module>>

    @Query("SELECT * from module_table ORDER BY dateCreated DESC")
    fun getNotesOrderedByDESCDate(): Flow<List<Module>>

}