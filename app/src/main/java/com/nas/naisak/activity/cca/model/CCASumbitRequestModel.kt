package com.nas.naisak.activity.cca.model

import com.google.gson.annotations.SerializedName

class CCASumbitRequestModel(
    @SerializedName("student_id") var student_id: String,
    @SerializedName("cca_days_id") var cca_days_id: String,
    @SerializedName("cca_days_details_ids") var cca_days_details_ids: String

)