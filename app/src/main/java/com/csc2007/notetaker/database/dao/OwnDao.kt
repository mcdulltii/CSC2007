package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csc2007.notetaker.database.Own
import com.csc2007.notetaker.database.OwnItem

@Dao
interface OwnDao {

    @Query("SELECT * FROM own_table, item_table WHERE own_table.itemId = item_table.id AND own_table.userId = :userId")
    suspend fun getOwnedItems(userId: Int): List<OwnItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(own: Own)

    @Query("SELECT * FROM own_table WHERE userId = :userId AND itemId = :itemId")
    suspend fun checkIfUserOwnItem(userId: Int, itemId: Int): Own

    @Query("DELETE From own_table")
    suspend fun deleteAll()
}