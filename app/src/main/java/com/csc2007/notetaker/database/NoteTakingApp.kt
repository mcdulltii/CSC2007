package com.csc2007.notetaker.database

import android.app.Application
import android.content.Context
import com.csc2007.notetaker.database.repository.UsersRepository
import kotlinx.coroutines.GlobalScope
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csc2007.notetaker.database.repository.ModulesRepository
import com.csc2007.notetaker.database.repository.NotesRepository
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModel

class NoteTakingApp : Application() {

    val userDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).userDao() }
    val noteDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).noteDao() }
    val moduleDao by lazy { NoteTakingDatabase.getDatabase(this, GlobalScope).moduleDao() }


    val Context.dataStore by preferencesDataStore(
        name = "UserPreference"
    )
    val repository by lazy { UsersRepository(userDao, dataStore) }


    val noteRepository by lazy { NotesRepository(noteDao)}

    val moduleRepository by lazy { ModulesRepository(moduleDao) }

}