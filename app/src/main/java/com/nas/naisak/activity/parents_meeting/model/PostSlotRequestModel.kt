package com.nas.naisak.activity.parents_meeting.model

import com.google.gson.annotations.SerializedName

class PostSlotRequestModel (
    @SerializedName("student_id") val student_id: String,
    @SerializedName("pta_time_slot_id") val pta_time_slot_id: String,
        )
