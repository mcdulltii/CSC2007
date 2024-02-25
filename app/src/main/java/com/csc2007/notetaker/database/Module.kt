package com.csc2007.notetaker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Module(
    val title: String,
    val dateCreated: Long,
    val imagePath: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)