package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

data class CredentialsDto(

    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String
)