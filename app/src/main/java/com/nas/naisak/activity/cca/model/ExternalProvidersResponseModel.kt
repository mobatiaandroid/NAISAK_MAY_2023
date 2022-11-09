package com.nas.naisak.activity.cca.model

data class ExternalProvidersResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val banner_image: String?,
        val lists: ArrayList<Lists?>?
    ) {
        data class Lists(
            val id: Int?,
            val title: String?,
            val url: String?
        )
    }
}