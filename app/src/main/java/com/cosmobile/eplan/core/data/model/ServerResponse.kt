package com.cosmobile.eplan.core.data.model

data class ServerResponse(
    val code: Int,
    val message: String
) {
    fun isOk() = code in 200..299
}