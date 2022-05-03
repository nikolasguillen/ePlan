package com.example.eplan.network.responses

import com.example.eplan.network.model.InterventionDto
import com.google.gson.annotations.SerializedName

class InterventionByIdResponse(
    @SerializedName("intervento")
    var intervention: InterventionDto
)