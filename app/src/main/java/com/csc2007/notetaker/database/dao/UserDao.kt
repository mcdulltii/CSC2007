package com.csc2007.notetaker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.csc2007.notetaker.database.Avatar
import com.csc2007.notetaker.database.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE email = :email  AND password = :password")
    fun login(email: String, password: ByteArray): User?

    @Query("SELECT points FROM user_table WHERE email = :email")
    fun getUserPoints(email: String): Int

    @Query("UPDATE user_table SET points = :points WHERE email = :email")
    fun updateUserPoints(points: Int, email: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): User

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserById(id: Int): User

    @Query("UPDATE user_table SET email = :email, username = :username WHERE id = :id")
    suspend fun updateEmailAndUserName(email: String, username: String, id: Int)

    @Query("UPDATE user_table SET password = :password WHERE id = :id")
    suspend fun updatePassword(password: ByteArray, id: Int)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("INSERT INTO avatar_table (userId) VALUES (:userId)")
    suspend fun createNewAvatar(userId: Int)

    @Query("SELECT secret FROM user_table WHERE email = :email")
    suspend fun getUserSecret(email: String): ByteArray
}