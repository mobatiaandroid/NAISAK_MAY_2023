package com.nas.naisak.fragment.cca.model

data class BannerResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val banner_image: String?,
        val cca_badge: Int?,
        val cca_edited_badge: Int?,
        val contact_email: String?,
        val description: String?
    )
}