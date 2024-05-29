package com.cosmobile.eplan.appointment_detail.data.model

import com.cosmobile.eplan.core.data.model.AppointmentListItemDto
import com.google.gson.annotations.SerializedName

class AppointmentByIdResponse(
    @SerializedName("appuntamento")
    var appointment: AppointmentListItemDto
)