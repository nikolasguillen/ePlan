package com.example.eplan.network.model

import com.example.eplan.domain.model.User
import com.google.gson.annotations.SerializedName

data class AppointmentDto(

    @SerializedName("id_appuntamenti")
    var appointmentId: String,

    @SerializedName("id_attivita")
    var activityId: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("start")
    var start: String,

    @SerializedName("end")
    var end: String,

    @SerializedName("pianificazione")
    var planning: Boolean,

    @SerializedName("contabilizza")
    var intervention: Boolean,

    @SerializedName("invitati")
    var invited: List<User>,

    @SerializedName("periodicita")
    var periodicity: String,

    @SerializedName("fine_periodicita")
    var periodicityEnd: String,

    @SerializedName("promemoria")
    var memo: Boolean,

    @SerializedName("tipo_promemoria")
    var memoType: List<String>,

    @SerializedName("preavviso_quantita")
    var warningTime: Int,

    @SerializedName("preavviso_unita")
    var warningUnit: String
)
