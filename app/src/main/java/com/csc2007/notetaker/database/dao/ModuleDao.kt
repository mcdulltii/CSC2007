package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.csc2007.notetaker.database.entity.Module
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleDao {

    @Upsert
    suspend fun upsertModule(module: Module)

    @Delete
    suspend fun deleteModule(module: Module)

    @Query("SELECT * FROM module")
    fun getAllNotes(): Flow<List<Module>>

    @Query("SELECT * FROM module ORDER BY title ASC")
    fun getNotesOrderedByASCTitle(): Flow<List<Module>>

    @Query("SELECT * from module ORDER BY title DESC")
    fun getNotesOrderedByDESCTitle(): Flow<List<Module>>

    @Query("SELECT * FROM module ORDER BY dateCreated ASC")
    fun getNotesOrderedByASCDate(): Flow<List<Module>>

    @Query("SELECT * from module ORDER BY dateCreated DESC")
    fun getNotesOrderedByDESCDate(): Flow<List<Module>>

}