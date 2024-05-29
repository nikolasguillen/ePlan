package com.cosmobile.eplan.core.data.model

import com.cosmobile.eplan.appointment_detail.domain.model.WarningUnit

class WarningTimeConverterImpl : WarningTimeConverter {
    override fun fromString(value: String, warningUnit: WarningUnit): Int {
        val parts = if (value.isNotBlank() && value.startsWith("-")) {
            value.drop(1).padStart(8, '0').padEnd(8, '0')
        } else {
            value.padStart(8, '0').padEnd(8, '0')
        }.split(":")

        val hours = parts[0].toIntOrNull() ?: 0
        val minutes = parts[1].toIntOrNull() ?: 0
        val seconds = parts[2].toIntOrNull() ?: 0

        val totalMinutes = (hours * 60) + minutes + (seconds / 60)

        return when (warningUnit) {
            WarningUnit.MINUTES -> totalMinutes
            WarningUnit.HOURS -> totalMinutes / 60
            WarningUnit.DAYS -> totalMinutes / 1440
        }
    }

    override fun toString(value: Int, warningUnit: WarningUnit): String {
        val totalMinutes = when (warningUnit) {
            WarningUnit.MINUTES -> value
            WarningUnit.HOURS -> value * 60
            WarningUnit.DAYS -> value * 1440
        }

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        val seconds = 0

        return "-" +
                hours.toString().padStart(2, '0') +
                ":" +
                minutes.toString().padStart(2, '0') +
                ":" +
                seconds.toString().padStart(2, '0')
    }

}