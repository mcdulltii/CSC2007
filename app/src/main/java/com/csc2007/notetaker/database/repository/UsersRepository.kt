package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.dao.UserDao
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences

class UsersRepository(private val userDao: UserDao, private val dataStore: DataStore<Preferences>) {

    val allUsers: Flow<List<User>> = userDao.getUsers()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}