package com.example.eplan.cache

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.eplan.cache.model.UserEntity

interface UserDao {

    @Insert
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT token FROM users WHERE username = :username ")
    suspend fun getUserToken(username: String): String
}