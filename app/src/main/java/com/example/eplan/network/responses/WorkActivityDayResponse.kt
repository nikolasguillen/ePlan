package com.example.eplan.network.responses

import com.example.eplan.network.model.WorkActivityDto
import com.google.gson.annotations.SerializedName

class WorkActivityDayResponse (

    @SerializedName("interventi")
    var workActivities: List<WorkActivityDto>
)