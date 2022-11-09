package com.nas.naisak.activity.cca.model

import com.google.gson.annotations.SerializedName

class CCACancelRequestModel (
    @SerializedName("student_id") var student_id: String,
    @SerializedName("cca_days_details_ids") var cca_days_details_ids: String
)

