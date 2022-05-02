package com.example.eplan.domain.util

import android.content.res.Resources
import com.example.eplan.R

enum class Periodicity(name: String) {
    NESSUNA(Resources.getSystem().getString(R.string.nessuna)),
    GIORNALIERA(Resources.getSystem().getString(R.string.giornaliera)),
    SETTIMANALE(Resources.getSystem().getString(R.string.settimanale)),
    BISETTIMANALE(Resources.getSystem().getString(R.string.bisettimanale)),
    MENSILE(Resources.getSystem().getString(R.string.mensile)),
    BIMESTRALE(Resources.getSystem().getString(R.string.bimestrale))
}