package com.example.eplan.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eplan.cache.model.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT token FROM users")
    suspend fun getUserToken(): String

    @Query("UPDATE users SET imageUri = :uri WHERE username = :username")
    suspend fun saveImageUri(username: String, uri: String)

    @Query("SELECT imageUri FROM users")
    suspend fun getImageUri(): String

    @Query("SELECT username FROM users")
    suspend fun getUsername(): String

    @Query("SELECT password FROM users")
    suspend fun getPassword(): String
}