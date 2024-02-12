package com.example.eplan.presentation.util

data class Dialog(
    val title: UiText? = null,
    val message: UiText,
    val dismissText: UiText? = null,
    val confirmText: UiText,
    val onDismiss: () -> Unit,
    val onConfirm: () -> Unit
)
