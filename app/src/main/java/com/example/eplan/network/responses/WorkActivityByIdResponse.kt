package com.example.eplan.network.responses

import com.example.eplan.network.model.WorkActivityDto
import com.google.gson.annotations.SerializedName

class WorkActivityByIdResponse(
    @SerializedName("intervento")
    var workActivity: WorkActivityDto
)