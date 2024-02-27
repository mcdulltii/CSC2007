package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import com.csc2007.notetaker.database.Own
import com.csc2007.notetaker.database.OwnItem
import com.csc2007.notetaker.database.dao.OwnDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OwnRepository(private val ownDao: OwnDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getOwnedItems(userId: Int): List<OwnItem> {
        return withContext(Dispatchers.IO) {
            ownDao.getOwnedItems(userId = userId)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(own: Own) {
        ownDao.insert(own)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun checkIfUserOwnItem(userId: Int, itemId: Int): Own {
        return withContext(Dispatchers.IO) {
            ownDao.checkIfUserOwnItem(userId, itemId)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        ownDao.deleteAll()
    }
}