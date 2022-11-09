package com.nas.naisak.commonmodels

import com.google.gson.annotations.SerializedName

class TimeSlotsRequestModel (
        @SerializedName("student_id") val student_id: String,
        @SerializedName("staff_id") val staff_id: String,
        @SerializedName("date") val date: String,
        )
