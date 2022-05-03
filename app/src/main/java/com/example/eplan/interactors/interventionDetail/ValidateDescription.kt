package com.example.eplan.interactors.interventionDetail

class ValidateDescription {

    fun execute(description: String): ValidationResult {
        if (description.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La descrizione non può essere vuota"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}