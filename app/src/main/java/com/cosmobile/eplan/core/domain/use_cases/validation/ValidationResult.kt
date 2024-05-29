package com.cosmobile.eplan.core.domain.use_cases.validation

import com.cosmobile.eplan.core.util.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)