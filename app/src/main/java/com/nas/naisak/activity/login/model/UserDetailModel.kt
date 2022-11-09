package com.nas.naisak.activity.login.model

import com.google.gson.annotations.SerializedName

class UserDetailModel (
    @SerializedName("id") val id: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("email") val email: String
)