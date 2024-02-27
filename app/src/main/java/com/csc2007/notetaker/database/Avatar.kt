package com.csc2007.notetaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "avatar_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        childColumns = ["userId"],
        parentColumns = ["id"]
)])
data class Avatar (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "hat") val hat: Int,
    @ColumnInfo(name = "accessory") val accessory: Int,
    @ColumnInfo(name = "shirt") val shirt: Int,
    @ColumnInfo(name = "pants") val pants: Int,
    @ColumnInfo(name = "shoes") val shoes: Int,
)