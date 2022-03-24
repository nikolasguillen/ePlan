package com.example.eplan.repository

import android.content.Context
import com.example.eplan.domain.model.WorkActivity

interface WorkActivityRepository {

    suspend fun getMonthActivities(userToken: String, month: Int, context: Context): List<WorkActivity>
}