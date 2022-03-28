package com.example.eplan.network.model

import com.google.gson.annotations.SerializedName

class UserDto(
    @SerializedName("name")
    var fullName: String
)