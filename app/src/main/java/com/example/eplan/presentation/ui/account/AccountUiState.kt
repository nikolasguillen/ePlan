package com.example.eplan.presentation.ui.account

import android.net.Uri
import com.example.eplan.presentation.util.Dialog

data class AccountUiState(
    val imageUri: Uri = Uri.EMPTY,
    val username: String = "",
    val accountItems: List<AccountItems> = listOf(
        AccountItems.Settings,
        AccountItems.TimeStats,
        AccountItems.VacationRequest
    ),
    val dialog: Dialog? = null
)
