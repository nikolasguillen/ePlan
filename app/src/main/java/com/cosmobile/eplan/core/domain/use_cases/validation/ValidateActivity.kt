package com.cosmobile.eplan.core.domain.use_cases.validation

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.UiText

class ValidateActivity {

    fun execute(activityName: String): ValidationResult {
        if (activityName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.validation_seleziona_attivita)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}