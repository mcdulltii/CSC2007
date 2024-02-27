package com.csc2007.notetaker.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "own_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        childColumns = ["userId"],
        parentColumns = ["id"]
    ), ForeignKey(
        entity = Item::class,
        childColumns = ["itemId"],
        parentColumns = ["id"]
    )])
data class Own (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "itemId") val itemId: Int
)