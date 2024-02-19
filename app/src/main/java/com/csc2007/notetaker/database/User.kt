package com.csc2007.notetaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "username") val userName: String,
    @ColumnInfo(name = "password") val password: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return password.contentEquals(other.password)
    }

    override fun hashCode(): Int {
        return password.contentHashCode()
    }
}