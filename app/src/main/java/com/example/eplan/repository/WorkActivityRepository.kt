package com.example.eplan.repository

import android.content.Context
import com.example.eplan.domain.model.User
import com.example.eplan.domain.model.WorkActivity

interface WorkActivityRepository {

    suspend fun getDayActivities(
        userToken: String,
        dayOfMonth: Int,
        month: Int,
        context: Context
    ): List<WorkActivity>

    suspend fun getActivityById(
        userToken: String,
        activityId: Int
    ): WorkActivity

    suspend fun updateWorkActivity(userToken: String, workActivity: WorkActivity)
}