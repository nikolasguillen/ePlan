package com.cosmobile.eplan.appointment_detail.domain.model

import androidx.annotation.StringRes
import com.cosmobile.eplan.R

sealed class Periodicity(val serverFieldName: String, @StringRes val humanReadableNameResId: Int) {
    data object None : Periodicity("Nessuna", R.string.nessuna)
    data object Daily : Periodicity("giornaliera", R.string.giornaliera)
    data object Weekly : Periodicity("settimanale", R.string.settimanale)
    data object Biweekly : Periodicity("bisettimanale", R.string.bisettimanale)
    data object Monthly : Periodicity("mensile", R.string.mensile)
    data object Bimonthly : Periodicity("bimestrale", R.string.bimestrale)
}