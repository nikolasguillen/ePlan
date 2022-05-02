package com.example.eplan.network.responses

import com.example.eplan.network.model.AppointmentDto
import com.google.gson.annotations.SerializedName

class AppointmentDayResponse(
    @SerializedName("appuntamenti")
    var appointments: List<AppointmentDto>
)