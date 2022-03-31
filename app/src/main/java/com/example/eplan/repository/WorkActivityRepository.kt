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
        activityId: String,
        context: Context
    ): WorkActivity

    suspend fun updateWorkActivity(userToken: String, workActivity: WorkActivity) /* TODO chidedere se ritorna qualcosa */

    suspend fun deleteWorkActivity(userToken: String, id: String) /* TODO chidedere se ritorna qualcosa */
}