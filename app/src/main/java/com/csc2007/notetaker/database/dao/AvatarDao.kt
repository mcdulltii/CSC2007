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

    @Query("SELECT * FROM avatar_table, item_table WHERE avatar_table.userId = :userId AND avatar_table.shirt = item_table.id")
    suspend fun getEquippedShirt(userId: Int): AvatarItem

    @Query("UPDATE avatar_table SET hat = :hatId WHERE userId = :userId")
    suspend fun equipHat(hatId: Int, userId: Int)

    @Query("UPDATE avatar_table SET accessory = :accessoryId WHERE userId = :userId")
    suspend fun equipAccessory(accessoryId: Int, userId: Int)

    @Query("UPDATE avatar_table SET shirt = :shirtId WHERE userId = :userId")
    suspend fun equipShirt(shirtId: Int, userId: Int)

    @Query("UPDATE avatar_table SET hat = null WHERE userId = :userId")
    suspend fun unEquipHat(userId: Int)

    @Query("UPDATE avatar_table SET accessory = null WHERE userId = :userId")
    suspend fun unEquipAccessory(userId: Int)

    @Query("UPDATE avatar_table SET shirt = null WHERE userId = :userId")
    suspend fun unEquipShirt(userId: Int)
}
