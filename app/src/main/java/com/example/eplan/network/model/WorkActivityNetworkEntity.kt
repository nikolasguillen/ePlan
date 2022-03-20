package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

data class WorkActivityNetworkEntity(

    @SerializedName("title")
    var title: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("start")
    var start: String,

    @SerializedName("duration")
    var duration: String,

    @SerializedName("id")
    var id: String,

    @SerializedName("color")
    var color: String,

    @SerializedName("end")
    var end: String
)