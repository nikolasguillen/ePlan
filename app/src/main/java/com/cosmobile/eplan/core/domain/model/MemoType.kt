package com.cosmobile.eplan.core.domain.model

sealed class MemoType(val name: String) {
    data object Email : MemoType("email")
    data object Notification : MemoType("notifica")
    data object None : MemoType("nessuna")
}