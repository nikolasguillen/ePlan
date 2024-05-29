package com.cosmobile.eplan.appointment_detail.domain.model

import android.content.Context
import com.cosmobile.eplan.R

enum class WarningUnit(private val nameResId: Int) {
    MINUTES(R.string.minuti),
    HOURS(R.string.ore),
    DAYS(R.string.giorni);

    /* Non la soluzione più elegante, ma sicuramente la più sicura per poter recuperare la stringa dalle risorse */
    fun getName(context: Context): String {
        return context.getString(nameResId)
    }
}