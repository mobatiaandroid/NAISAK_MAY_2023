package com.nas.naisak.activity.gallery.model

import java.io.Serializable

data class GetPhotoResponseModel(
    val `data`: List<Data?>?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val album_id: Int?,
        val created_at: String?,
        val id: Int?,
        val image: String?,
        val title: String?,
        val updated_at: String?
    ): Serializable
}