package com.cosmobile.eplan.appointments_list.data.model

import com.cosmobile.eplan.core.data.model.AppointmentListItemDto
import com.google.gson.annotations.SerializedName

class AppointmentsByDayResponse(
    @SerializedName("appuntamenti")
    var appointments: List<AppointmentListItemDto>
)