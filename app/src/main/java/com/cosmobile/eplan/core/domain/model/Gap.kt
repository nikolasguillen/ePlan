package com.cosmobile.eplan.core.domain.model

import com.cosmobile.eplan.core.util.UiText
import java.time.LocalDate
import java.time.LocalTime

data class Gap(
    override val id: String = "",
    override val activityId: String = "",
    override val activityIdError: UiText? = null,
    override val activityName: String = "",
    override val title: String = "",
    override val description: String = "",
    override val date: LocalDate,
    override val start: LocalTime,
    override val end: LocalTime,
    override val color: String = ""
) : WorkActivity