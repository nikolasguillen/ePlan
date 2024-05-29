package com.cosmobile.eplan.core.data.model

import com.google.gson.annotations.SerializedName

data class InterventionDto(

    @SerializedName("id_attivita")
    val idAttivita: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("descrizione")
    val description: String?,

    @SerializedName("start")
    val start: String?,

    @SerializedName("id_interventi")
    val id: String?,

    @SerializedName("ore_spostamento")
    val moveTime: String?,

    @SerializedName("km_spostamento")
    val moveDistance: String?,

    @SerializedName("color")
    val color: String?,

    @SerializedName("end")
    val end: String?
)