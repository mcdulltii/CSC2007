package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import com.csc2007.notetaker.database.Item
import com.csc2007.notetaker.database.dao.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: Flow<List<Item>> = itemDao.getItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getItemById(id: Int): Item {
        return withContext(Dispatchers.IO) {
            itemDao.getItemById(id)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(item: Item) {
        itemDao.insert(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        itemDao.deleteAll()
    }
}