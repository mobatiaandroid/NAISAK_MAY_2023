package com.nas.naisak.activity.parents_meeting.model

import com.google.gson.annotations.SerializedName

class PostSlotResponseModel
        (
        @SerializedName("status") val status: Int,
        @SerializedName("message") val message: String,
        @SerializedName("data") val data: PlotDataResponseModel,
)
