package com.example.eplan.repository

import android.content.Context
import com.example.eplan.domain.model.WorkActivity

interface WorkActivityRepository {

    suspend fun getDayActivities(
        userToken: String,
        query: String,
        context: Context
    ): List<WorkActivity>

    suspend fun getActivityById(
        userToken: String,
        activityId: String
    ): WorkActivity

    suspend fun updateWorkActivity(userToken: String, workActivity: WorkActivity)
}