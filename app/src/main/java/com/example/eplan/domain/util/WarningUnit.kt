package com.example.eplan.domain.util

import android.content.res.Resources
import com.example.eplan.R

enum class WarningUnit(name: String) {
    MINUTI(Resources.getSystem().getString(R.string.minuti)),
    ORE(Resources.getSystem().getString(R.string.ore)),
    GIORNI(Resources.getSystem().getString(R.string.giorni))
}