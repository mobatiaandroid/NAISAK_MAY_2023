package com.nas.naisak.commonmodels

data class StaffListByStudentResponseModel(
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
            val image_url: String?,
            val name: String?
        )
    }
}