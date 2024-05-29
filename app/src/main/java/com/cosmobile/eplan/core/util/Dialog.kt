package com.cosmobile.eplan.core.util

data class Dialog(
    val title: UiText? = null,
    val message: UiText,
    val dismissText: UiText? = null,
    val confirmText: UiText,
    val onDismiss: () -> Unit,
    val onConfirm: () -> Unit
)
