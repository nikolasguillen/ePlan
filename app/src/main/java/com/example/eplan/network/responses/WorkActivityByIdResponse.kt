package com.example.eplan.network.responses

import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.network.model.WorkActivityDto
import com.google.gson.annotations.SerializedName

class WorkActivityByIdResponse(
    @SerializedName("activity")
    var workActivity: WorkActivityDto
)