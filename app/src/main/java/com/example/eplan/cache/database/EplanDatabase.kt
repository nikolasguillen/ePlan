package com.example.eplan.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eplan.cache.database.dao.WorkActivityDao
import com.example.eplan.cache.model.WorkActivityEntity

private const val DATABASE_VERSION = 1

@Database(
    entities = [WorkActivityEntity::class],
    version = DATABASE_VERSION
)
abstract class EplanDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "eplan_db"
        fun buildDatabase(context: Context): EplanDatabase {
            return Room.databaseBuilder(
                context,
                EplanDatabase::class.java,
                DATABASE_NAME
            )
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun workActivityDao(): WorkActivityDao
}