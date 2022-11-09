package com.nas.naisak.activity.cca.model

data class CCAInfoResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val lists: ArrayList<Lists?>?
    ) {
        data class Lists(
            val id: Int?,
            val title: String?,
            val url: String?
        )
    }
}