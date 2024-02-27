package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csc2007.notetaker.database.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item_table ORDER BY id ASC")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE id = :id")
    suspend fun getItemById(id: Int): Item

    @Query("SELECT * FROM item_table WHERE name = :name")
    suspend fun getItemByName(name: String): Item

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Query("DELETE FROM item_table")
    suspend fun deleteAll()
}