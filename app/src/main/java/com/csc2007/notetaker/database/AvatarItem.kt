package com.csc2007.notetaker.database

import androidx.room.ColumnInfo

data class AvatarItem (
    @ColumnInfo(name = "userId") val userId: Int? = 0,
    @ColumnInfo(name = "name") val name: String? = "",
    @ColumnInfo(name = "type") val type: String? = "",
    @ColumnInfo(name = "rarity") val rarity: String? = "",
    @ColumnInfo(name = "image") val image: String? = ""
)