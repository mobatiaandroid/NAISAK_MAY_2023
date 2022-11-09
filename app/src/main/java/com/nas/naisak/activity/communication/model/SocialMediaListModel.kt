package com.nas.naisak.activity.communication.model

import com.google.gson.annotations.SerializedName

class SocialMediaListModel(
    @SerializedName("title") val title: String = "",
    @SerializedName("url") val url: String = ""
)
