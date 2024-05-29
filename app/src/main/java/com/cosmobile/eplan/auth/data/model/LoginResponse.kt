package com.cosmobile.eplan.auth.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    val data: LoginResponseData?
) {
    fun isOk() = statusCode in 200..299
}

data class LoginResponseData(
    @SerializedName("nome")
    val name: String,
    @SerializedName("cognome")
    val surname: String,
    @SerializedName("ore_checkmail")
    val checkmailHours: String
)