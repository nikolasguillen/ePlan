package com.cosmobile.eplan.core.data.model

import com.cosmobile.eplan.appointment_detail.domain.model.WarningUnit

interface WarningTimeConverter {
    fun fromString(value: String, warningUnit: WarningUnit): Int
    fun toString(value: Int, warningUnit: WarningUnit): String
}