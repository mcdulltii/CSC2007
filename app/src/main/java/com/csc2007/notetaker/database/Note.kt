package com.csc2007.notetaker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val dateAdded: Long,
    val content: String,
    val moduleId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
