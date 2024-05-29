package com.cosmobile.eplan.auth.data.model

import com.google.gson.annotations.SerializedName

data class CredentialsDto(

    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String
)