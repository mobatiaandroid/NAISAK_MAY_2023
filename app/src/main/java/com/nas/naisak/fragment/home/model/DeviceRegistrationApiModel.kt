package com.nas.naisak.fragment.home.model

import com.google.gson.annotations.SerializedName

class DeviceRegistrationApiModel (
    @SerializedName("devicetype") val devicetype: Int,
    @SerializedName("deviceid") val deviceid: String,
    @SerializedName("device_identifier") val device_identifier: String
)