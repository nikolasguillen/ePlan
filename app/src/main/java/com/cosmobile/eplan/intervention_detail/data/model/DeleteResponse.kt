package com.cosmobile.eplan.intervention_detail.data.model

import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    val result: String,
    @SerializedName("msg")
    val message: String
)
