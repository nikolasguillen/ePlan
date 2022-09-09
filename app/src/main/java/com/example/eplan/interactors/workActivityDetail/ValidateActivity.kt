package com.example.eplan.interactors.workActivityDetail

class ValidateActivity {

    fun execute(activityName: String, activitiesList: List<String>): ValidationResult {
        if (activityName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Seleziona un'attività"
            )
        }
        if (!activitiesList.contains(activityName)) {
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