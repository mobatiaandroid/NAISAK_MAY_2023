package com.nas.naisak.fragment.home.model

import com.google.gson.annotations.SerializedName

class Bannerimages (
    @SerializedName("banner_images") val banner_images: ArrayList<String>,
    @SerializedName("android_app_version") val android_app_version: String

)
