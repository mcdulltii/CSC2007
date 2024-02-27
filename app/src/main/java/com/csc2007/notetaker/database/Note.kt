package com.csc2007.notetaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "dateAdded") val dateAdded: Long,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "moduleId") val moduleId: Int,
)
