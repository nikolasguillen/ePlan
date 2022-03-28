package com.example.eplan.network.responses

import com.example.eplan.network.model.UserDto
import com.google.gson.annotations.SerializedName

class UsersListResponse(
    @SerializedName("users")
    var users: List<UserDto>
)
