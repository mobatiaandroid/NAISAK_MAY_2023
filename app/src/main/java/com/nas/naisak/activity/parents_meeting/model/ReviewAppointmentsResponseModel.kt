package com.nas.naisak.activity.parents_meeting.model

data class ReviewAppointmentsResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val lists: ArrayList<Lists?>?
    ) {
        data class Lists(
            val book_end_date: String?,
            val booking_open: String?,
            val date: String?,
            val end_time: String?,
            val id: Int?,
            val pta_time_slot_id: Int?,
            val room: String?,
            val staff: String?,
            val staff_id: Int?,
            val start_time: String?,
            val status: Int?,
            val student: String?,
            val student_class: String?,
            val student_id: Int?,
            val student_photo: String?,
            val vpml: String?
        )
    }
}