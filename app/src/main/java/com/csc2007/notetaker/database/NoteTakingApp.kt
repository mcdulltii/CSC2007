package com.csc2007.notetaker.database

import android.app.Application
import android.content.Context
import com.csc2007.notetaker.database.repository.UsersRepository
import kotlinx.coroutines.GlobalScope
import androidx.datastore.preferences.preferencesDataStore

class NoteTakingApp : Application() {

    val userDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).userDao() }
    val Context.dataStore by preferencesDataStore(
        name = "UserPreference"
    )
    val repository by lazy { UsersRepository(userDao, dataStore) }
}