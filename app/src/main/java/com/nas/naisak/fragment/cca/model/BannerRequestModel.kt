package com.nas.naisak.fragment.cca.model

import com.google.gson.annotations.SerializedName

class BannerRequestModel(
    @SerializedName("limit") val limit: String,
    @SerializedName("start") val start: String,
)