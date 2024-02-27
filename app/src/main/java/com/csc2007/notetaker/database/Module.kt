package com.csc2007.notetaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module_table")
data class Module(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "dateCreated") val dateCreated: Long,
    @ColumnInfo(name = "imagePath") val imagePath: String,
)