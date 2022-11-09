package com.nas.naisak.commonmodels

import com.google.gson.annotations.SerializedName

class SendStaffMailApiModel(
    @SerializedName("staff_email") val staff_email: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String
)
