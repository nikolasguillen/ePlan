package com.cosmobile.eplan.account

import com.cosmobile.eplan.core.util.Dialog

data class AccountUiState(
    val username: String = "",
    val accountItems: List<AccountItems> = listOf(
        AccountItems.Settings,
        AccountItems.TimeStats,
        AccountItems.VacationRequest
    ),
    val dialog: Dialog? = null
)