package com.cosmobile.eplan.intervention_detail.data.model

import com.cosmobile.eplan.core.data.model.InterventionDto
import com.google.gson.annotations.SerializedName

class InterventionByIdResponse(
    @SerializedName("intervento")
    var intervention: InterventionDto
)