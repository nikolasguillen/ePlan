package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

data class VacationRequestDto(

    @SerializedName("data_inizio")
    var startDate: String,

    @SerializedName("data_fine")
    var endDate: String?
)