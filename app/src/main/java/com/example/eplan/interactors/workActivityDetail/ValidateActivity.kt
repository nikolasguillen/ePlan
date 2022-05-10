package com.example.eplan.interactors.workActivityDetail

class ValidateActivity {

    fun execute(activityName: String, activitiesMap: Map<String, String>): ValidationResult {
        if (activityName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Seleziona un'attività"
            )
        }
        if (!activitiesMap.containsValue(activityName)) {
            return ValidationResult(
                successful = false,
                errorMessage = "L'attività selezionata non è valida"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}