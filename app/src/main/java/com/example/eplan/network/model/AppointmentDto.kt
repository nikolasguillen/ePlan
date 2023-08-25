package com.example.eplan.network.model

import com.example.eplan.domain.model.User
import com.google.gson.annotations.SerializedName

data class AppointmentDto(

    @SerializedName("id_appuntamenti")
    var appointmentId: String,

    @SerializedName("id_attivita")
    var activityId: String,

    @SerializedName("titolo")
    var title: String,

    @SerializedName("descrizione")
    var description: String,

    @SerializedName("start")
    var start: String,

    @SerializedName("end")
    var end: String,

    @SerializedName("pianificazione")
    var planning: Boolean,

    @SerializedName("contabilizzato")
    var intervention: Boolean,

    @SerializedName("invitati")
    var invited: List<User>,

    @SerializedName("periodicita")
    var periodicity: String,

    @SerializedName("periodicita_fine")
    var periodicityEnd: String,

    @SerializedName("promemoria")
    var memo: Boolean,

    @SerializedName("promemoria_mezzo")
    var memoType: List<String>,

    @SerializedName("preavviso_quantita")
    var warningTime: Int,

    @SerializedName("preavviso_unita")
    var warningUnit: String
)
