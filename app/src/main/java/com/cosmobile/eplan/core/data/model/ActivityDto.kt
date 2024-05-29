package com.cosmobile.eplan.core.data.model

import com.google.gson.annotations.SerializedName

data class ActivityDto(

    @SerializedName("id")
    var activityId: String,

    @SerializedName("description")
    var activityName: String
)