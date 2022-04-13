package com.example.eplan.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eplan.cache.model.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT token FROM users")
    suspend fun getUserToken(): String
}