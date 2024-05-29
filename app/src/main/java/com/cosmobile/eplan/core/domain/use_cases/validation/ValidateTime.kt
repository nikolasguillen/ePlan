package com.cosmobile.eplan.core.domain.use_cases.validation

import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.util.UiText
import com.cosmobile.eplan.core.util.acceptableTimeInterval
import java.time.LocalTime

class ValidateTime {

    fun execute(start: LocalTime, end: LocalTime): ValidationResult {
        if (!acceptableTimeInterval(start = start, end = end)) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.validation_orario)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}