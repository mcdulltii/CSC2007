package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.csc2007.notetaker.database.Avatar
import com.csc2007.notetaker.database.AvatarItem

@Dao
interface AvatarDao {

    @Query("SELECT * FROM avatar_table WHERE avatar_table.userId = :userId")
    suspend fun getUserAvatar(userId: Int): Avatar

    @Query("SELECT * FROM avatar_table, item_table WHERE avatar_table.userId = :userId AND avatar_table.hat = item_table.id")
    suspend fun getEquippedHat(userId: Int): AvatarItem

    @Query("SELECT * FROM avatar_table, item_table WHERE avatar_table.userId = :userId AND avatar_table.accessory = item_table.id")
    suspend fun getEquippedAccessory(userId: Int): AvatarItem

    @Query("UPDATE avatar_table SET hat = :hatId")
    suspend fun equipHat(hatId: Int)
}
