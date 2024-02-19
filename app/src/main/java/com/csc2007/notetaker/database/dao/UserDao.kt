package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.csc2007.notetaker.database.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE email = :email  AND password = :password")
    fun login(email: String, password: ByteArray): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}