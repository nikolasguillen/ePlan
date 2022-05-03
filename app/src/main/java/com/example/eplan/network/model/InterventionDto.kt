package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

data class InterventionDto(

    @SerializedName("id_attivita")
    var idAttivita: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("start")
    var start: String,

//    TODO controllare se serve ancora
//    @SerializedName("durata")
//    var duration: String,

    @SerializedName("id_interventi")
    var id: String,

    @SerializedName("ore_spostamento")
    var moveTime: String,

    @SerializedName("km_spostamento")
    var moveDistance: String,

    @SerializedName("color")
    var color: String,

    @SerializedName("end")
    var end: String
)