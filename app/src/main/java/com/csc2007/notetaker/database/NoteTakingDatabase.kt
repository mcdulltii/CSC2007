package com.csc2007.notetaker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.csc2007.notetaker.database.dao.AvatarDao
import com.csc2007.notetaker.database.dao.ItemDao
import com.csc2007.notetaker.database.dao.ModuleDao
import com.csc2007.notetaker.database.dao.NoteDao
import com.csc2007.notetaker.database.dao.OwnDao
import com.csc2007.notetaker.database.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Item::class, Own::class, Avatar::class, Note::class, Module::class], version = 7)
abstract class NoteTakingDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun itemDao(): ItemDao
    abstract fun ownDao(): OwnDao
    abstract fun avatarDao(): AvatarDao
    abstract fun noteDao(): NoteDao
    abstract fun moduleDao(): ModuleDao

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
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.itemDao())
                    }
                }
            }

            suspend fun populateDatabase(itemDao: ItemDao) {
                // Delete all content here
                itemDao.deleteAll()

                var item = Item(id = 0, name = "Penguin Hat", type = "Hat", rarity = "Rare", image = "hat_1")
                itemDao.insert(item)
                item = Item(id = 1, name = "Santa Boy Hat", type = "Hat", rarity = "Epic", image = "santa_boy_hat")
                itemDao.insert(item)
                item = Item(id = 2, name = "Janus Wig", type = "Hat", rarity = "Legendary", image = "janus_wig")
                itemDao.insert(item)
            }
        }
    }
}