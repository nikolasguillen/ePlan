package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

data class TimeStatsDto(

    @SerializedName("data")
    var date: String,

    @SerializedName("ore_normali")
    var standardTime: Double,

    @SerializedName("straordinari")
    var overtime: Double,

    @SerializedName("ferie_permessi")
    var vacation: Double,

    @SerializedName("malattia")
    var disease: Double
)