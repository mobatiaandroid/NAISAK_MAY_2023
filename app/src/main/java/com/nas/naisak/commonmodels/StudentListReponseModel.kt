package com.nas.naisak.commonmodels

data class StudentListReponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val lists: ArrayList<Lists?>?
    ) {
        data class Lists(
            val house: String?,
            val id: Int?,
            val name: String?,
            val photo: String?,
            val section: String?,
            val student_class: String?,
            val wallet: String?
        )
    }
}