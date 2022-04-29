package com.example.eplan.cache

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eplan.cache.model.UserEntity

@Dao
interface UserDao {

    @Insert
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
}