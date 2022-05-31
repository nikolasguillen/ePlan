package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

data class TimeStatsDto(

    @SerializedName("data")
    var date: String,

    @SerializedName("ore_normali")
    var standardTime: Int,

    @SerializedName("straordinari")
    var overtime: Int,

    @SerializedName("ferie_permessi")
    var vacation: Int,

    @SerializedName("malattia")
    var disease: Int
)