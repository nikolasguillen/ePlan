package com.cosmobile.eplan.core.domain.model

data class Activity(
    val id: String,
    val name: String
) {
    fun isRegular(): Boolean {
        return isVacation().not() && isPermission().not() && isDisease().not()
    }

    fun isVacation(): Boolean {
        return name.contains("Ferie", true) || name.contains("Festività", true)
    }

    fun isPermission(): Boolean {
        return name.contains("Permesso", true)
    }

    fun isDisease(): Boolean {
        return name.contains("Malattia", true)
    }
}