package com.example.eplan.interactors.workActivityDetail

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)