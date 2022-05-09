package com.example.eplan.interactors.workActivityDetail

class ValidateActivityId {

    fun execute(activityId: String, activityIdList: List<String>): ValidationResult {
        if (activityId.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Seleziona un'attività"
            )
        }
        if (activityIdList.contains(activityId)) {
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