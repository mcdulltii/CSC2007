package com.csc2007.notetaker.database

import android.app.Application
import android.content.Context
import com.csc2007.notetaker.database.repository.UsersRepository
import kotlinx.coroutines.GlobalScope
import androidx.datastore.preferences.preferencesDataStore
import com.csc2007.notetaker.database.repository.AvatarRepository
import com.csc2007.notetaker.database.repository.ItemRepository
import com.csc2007.notetaker.database.repository.OwnRepository

class NoteTakingApp : Application() {

    val userDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).userDao() }
    val itemDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).itemDao() }
    val ownDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).ownDao() }
    val avatarDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).avatarDao() }

    val Context.dataStore by preferencesDataStore(
        name = "UserPreference"
    )
    val userRepository by lazy { UsersRepository(userDao, dataStore) }
    val itemRepository by lazy { ItemRepository(itemDao) }
    val ownRepository by lazy { OwnRepository(ownDao) }
    val avatarRepository by lazy { AvatarRepository(avatarDao) }
}