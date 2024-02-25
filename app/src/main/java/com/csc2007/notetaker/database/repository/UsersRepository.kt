package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.csc2007.notetaker.database.entity.User
import com.csc2007.notetaker.database.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UsersRepository(private val userDao: UserDao, private val dataStore: DataStore<Preferences>) {

    val allUsers: Flow<List<User>> = userDao.getUsers()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun login(email: String, password: ByteArray): User {
        return withContext(Dispatchers.IO) {
            userDao.login(email, password)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}