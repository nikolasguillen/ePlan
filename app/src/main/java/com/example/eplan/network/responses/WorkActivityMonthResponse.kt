package com.example.eplan.network.responses

import com.example.eplan.network.model.WorkActivityDto
import com.google.gson.annotations.SerializedName

class WorkActivityMonthResponse (

    @SerializedName("")
    var workActivities: List<WorkActivityDto>
)