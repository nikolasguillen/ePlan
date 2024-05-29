package com.cosmobile.eplan.core.data.model

import com.google.gson.annotations.SerializedName

class RefreshTokenResponse(

    @SerializedName("statusCode")
    var statusCode: Int,

    @SerializedName("message")
    var message: String
)