package com.cosmobile.eplan.interventions_list.data.model

import com.cosmobile.eplan.core.data.model.InterventionDto
import com.google.gson.annotations.SerializedName

class InterventionsByDayResponse(

    @SerializedName("interventi")
    var interventions: List<InterventionDto>
)