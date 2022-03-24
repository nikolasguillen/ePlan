package com.example.eplan.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workActivities")
data class WorkActivityEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "start")
    var start: String,

    @ColumnInfo(name = "end")
    var end: String,

    @ColumnInfo(name = "duration")
    var duration: String,

    @ColumnInfo(name = "color")
    var color: String



)