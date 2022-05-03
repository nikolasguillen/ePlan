package com.example.eplan.interactors.interventionDetail

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)