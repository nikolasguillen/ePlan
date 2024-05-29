package com.cosmobile.eplan.core.domain.use_cases.validation

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.UiText

class ValidateDescription {

    fun execute(description: String): ValidationResult {
        if (description.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.validation_descrizione_vuota)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}