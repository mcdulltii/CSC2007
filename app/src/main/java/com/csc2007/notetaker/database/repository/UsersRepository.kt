package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.dao.UserDao
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepository(private val userDao: UserDao, private val dataStore: DataStore<Preferences>) {

    val allUsers: Flow<List<User>> = userDao.getUsers()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun login(email: String, password: String): User {
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