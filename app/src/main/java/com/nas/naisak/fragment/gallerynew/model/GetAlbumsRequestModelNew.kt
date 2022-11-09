package com.nas.naisak.fragment.gallerynew.model

import com.google.gson.annotations.SerializedName

class GetAlbumsRequestModelNew (
    @SerializedName("limit") val limit: String,
    @SerializedName("start") val start: String,
)
