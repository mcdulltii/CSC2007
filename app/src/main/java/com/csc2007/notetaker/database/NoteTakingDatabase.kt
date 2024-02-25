package com.csc2007.notetaker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.csc2007.notetaker.database.dao.ModuleDao
import com.csc2007.notetaker.database.dao.NoteDao
import com.csc2007.notetaker.database.dao.UserDao
import com.csc2007.notetaker.database.entity.Module
import com.csc2007.notetaker.database.entity.Note
import com.csc2007.notetaker.database.entity.User
import kotlinx.coroutines.CoroutineScope

@Database(entities = [User::class, Note::class, Module::class], version = 6)
abstract class NoteTakingDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

    abstract fun noteDao(): NoteDao

    abstract fun moduleDao() : ModuleDao



    companion object {
        @Volatile
        private var INSTANCE: NoteTakingDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : NoteTakingDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteTakingDatabase::class.java,
                    "notetaking_database"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(NoteTakingDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class NoteTakingDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}