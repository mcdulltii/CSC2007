package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.csc2007.notetaker.database.Avatar
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UsersRepository(private val userDao: UserDao, private val dataStore: DataStore<Preferences>) {

    val allUsers: Flow<List<User>> = userDao.getUsers()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun login(email: String, password: ByteArray): User? {
        return withContext(Dispatchers.IO) {
            userDao.login(email, password)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastUser(): User {
        return withContext(Dispatchers.IO) {
            userDao.getLastUser()
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUserSecret(email: String): ByteArray {
        return withContext(Dispatchers.IO) {
            userDao.getUserSecret(email)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateEmailAndUserName(email: String, username: String, id: Int) {
        userDao.updateEmailAndUserName(email, username, id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updatePassword(password: ByteArray, id: Int) {
        userDao.updatePassword(password, id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUserById(id: Int): User {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(id)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun createNewAvatar(userId: Int) {
        userDao.createNewAvatar(userId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUserPoints(email: String): Int {
        return withContext(Dispatchers.IO) {
            userDao.getUserPoints(email)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateUserPoints(points: Int, email: String) {
        return withContext(Dispatchers.IO) {
            userDao.updateUserPoints(points, email)
        }
    }
}