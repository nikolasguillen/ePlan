package com.cosmobile.eplan.core.domain.model

import com.cosmobile.eplan.core.util.UiText
import java.time.LocalDate
import java.time.LocalTime

interface WorkActivity {
    val id: String
    val activityId: String
    val activityIdError: UiText?
    val activityName: String
    val title: String
    val description: String
    val date: LocalDate
    val start: LocalTime
    val end: LocalTime
    val color: String
}