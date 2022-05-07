package com.example.eplan.domain.util

import android.content.Context
import android.content.res.Resources
import com.example.eplan.R

enum class Periodicity(private val nameResId: Int) {
    NONE(R.string.nessuna),
    DAILY(R.string.giornaliera),
    WEEKLY(R.string.settimanale),
    BIWEEKLY(R.string.bisettimanale),
    MONTHLY(R.string.mensile),
    BIMONTHLY(R.string.bimestrale);


    /* Non la soluzione più elegante, ma sicuramente la più sicura per poter recuperare la stringa dalle risorse */
    fun getName(context: Context): String {
        return context.getString(nameResId)
    }
}