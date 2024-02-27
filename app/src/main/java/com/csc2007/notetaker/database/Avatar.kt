package com.csc2007.notetaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "avatar_table")
data class Avatar (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "hat") val hat: Int? = null,
    @ColumnInfo(name = "accessory") val accessory: Int? = null,
    @ColumnInfo(name = "shirt") val shirt: Int? = null,
    @ColumnInfo(name = "pants") val pants: Int? = null,
    @ColumnInfo(name = "shoes") val shoes: Int? = null,
)