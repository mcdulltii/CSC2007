package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import com.csc2007.notetaker.database.Avatar
import com.csc2007.notetaker.database.AvatarItem
import com.csc2007.notetaker.database.dao.AvatarDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AvatarRepository(private val avatarDao: AvatarDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUserAvatar(userId: Int): Avatar {
        return withContext(Dispatchers.IO) {
            avatarDao.getUserAvatar(userId)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getEquippedHat(userId: Int): AvatarItem {
        return withContext(Dispatchers.IO) {
            avatarDao.getEquippedHat(userId)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getEquippedAccessory(userId: Int): AvatarItem {
        return withContext(Dispatchers.IO) {
            avatarDao.getEquippedAccessory(userId)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun equipHat(hatId: Int) {
        return withContext(Dispatchers.IO) {
            avatarDao.equipHat(hatId)
        }
    }
}