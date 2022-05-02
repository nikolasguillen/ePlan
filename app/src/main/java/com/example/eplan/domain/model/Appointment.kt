package com.example.eplan.domain.model

import android.content.res.Resources
import androidx.compose.ui.text.decapitalize
import com.example.eplan.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class Periodicity(name: String) {
    NESSUNA(Resources.getSystem().getString(R.string.nessuna)),
    GIORNALIERA(Resources.getSystem().getString(R.string.giornaliera)),
    SETTIMANALE(Resources.getSystem().getString(R.string.settimanale)),
    BISETTIMANALE(Resources.getSystem().getString(R.string.bisettimanale)),
    MENSILE(Resources.getSystem().getString(R.string.mensile)),
    BIMESTRALE(Resources.getSystem().getString(R.string.bimestrale))
}

enum class WarningUnit(name: String) {
    MINUTI(Resources.getSystem().getString(R.string.minuti)),
    ORE(Resources.getSystem().getString(R.string.ore)),
    GIORNI(Resources.getSystem().getString(R.string.giorni))
}

data class Appointment(
    val activityId: String = "",
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val start: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
    val end: LocalTime = LocalTime.now().plusMinutes(10).truncatedTo(ChronoUnit.MINUTES),
    val planning: Boolean = false,
    val intervention: Boolean = false,
    val invited: Map<User, Boolean>,
    val periodicity: Periodicity = Periodicity.NESSUNA,
    val periodicityEnd: LocalDate = LocalDate.now(),
    val memo: Boolean = false,
    val warningTime: Int,
    val warningUnit: WarningUnit
) {

}