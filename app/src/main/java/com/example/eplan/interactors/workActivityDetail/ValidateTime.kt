package com.example.eplan.interactors.workActivityDetail

import com.example.eplan.presentation.util.acceptableTimeInterval
import java.time.LocalTime

class ValidateTime {

    fun execute(start: LocalTime, end: LocalTime): ValidationResult {
        if (!acceptableTimeInterval(start = start, end = end)) {
            return ValidationResult(
                successful = false,
                errorMessage = "L'ora di fine intervento deve essere successiva a quella di inizio!"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}