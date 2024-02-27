package com.csc2007.notetaker.database.repository

import androidx.annotation.WorkerThread
import com.csc2007.notetaker.database.dao.ModuleDao
import com.csc2007.notetaker.database.Module
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ModulesRepository(private val moduleDao: ModuleDao) {

    val modulesOrderedByASCTitle: Flow<List<Module>> = moduleDao.getNotesOrderedByASCTitle()
    val modulesOrderedByDESCTitle: Flow<List<Module>> = moduleDao.getNotesOrderedByDESCTitle()

    val modulesOrderedByASCDate: Flow<List<Module>> = moduleDao.getNotesOrderedByASCDate()
    val modulesOrderedByDESCDate: Flow<List<Module>> = moduleDao.getNotesOrderedByDESCDate()

    val modules: Flow<List<Module>> = moduleDao.getAllNotes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun upsertModule(module: Module) {
        withContext(Dispatchers.IO) {
            moduleDao.upsertModule(module)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteModule(module: Module) {
        withContext(Dispatchers.IO) {
            moduleDao.deleteModule(module)
        }
    }
}
