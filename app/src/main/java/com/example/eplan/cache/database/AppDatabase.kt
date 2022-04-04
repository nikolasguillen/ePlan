package com.example.eplan.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eplan.cache.UserDao
import com.example.eplan.cache.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        val DATABASE_NAME = "eplan_db"
    }
}