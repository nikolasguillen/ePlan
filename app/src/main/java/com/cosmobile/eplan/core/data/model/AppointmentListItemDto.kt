package com.cosmobile.eplan.core.data.model

import com.cosmobile.eplan.appointment_detail.domain.model.User
import com.google.gson.annotations.SerializedName

data class AppointmentListItemDto(

    @SerializedName("id_appuntamenti")
    val appointmentId: String?,

    @SerializedName("id_attivita")
    val activityId: String?,

    @SerializedName("titolo")
    val title: String?,

    @SerializedName("descrizione")
    val description: String?,

    @SerializedName("start")
    val start: String?,

    @SerializedName("end")
    val end: String?,

    @SerializedName("pianificazione")
    val planning: Int?,

    @SerializedName("contabilizzato")
    val accounted: Int?,

    @SerializedName("invitati")
    val invited: List<User>?,

    @SerializedName("periodicita")
    val periodicity: String?,

    @SerializedName("periodicita_fine")
    val periodicityEnd: String?,

    @SerializedName("promemoria")
    val memo: Boolean?,

    @SerializedName("promemoria_mezzo")
    val memoType: String?,

    @SerializedName("promemoria_preavviso")
    val warningTime: String?, // TODO arriva una stringa nel formato ad es "-0:30:0"

    @SerializedName("preavviso_unita")
    val warningUnit: String?,

    @SerializedName("color")
    val color: String?
)
