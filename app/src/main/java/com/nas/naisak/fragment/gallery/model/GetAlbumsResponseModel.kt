package com.nas.naisak.fragment.gallery.model

data class GetAlbumsResponseModel(
    val `data`: ArrayList<Data?>?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val cover_image: String?,
        val created_at: String?,
        val id: Int?,
        val title: String?,
        val updated_at: String?
    )
}