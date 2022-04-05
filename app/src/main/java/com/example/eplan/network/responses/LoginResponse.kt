package com.example.eplan.network.responses

import com.google.gson.annotations.SerializedName

class LoginResponse(

    @SerializedName("statusCode")
    var statusCode: Int,

    @SerializedName("message")
    var message: String
)