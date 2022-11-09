package com.nas.naisak.commonmodels

data class TimeSlotsResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val lists: ArrayList<Lists?>?
    )
    { data class Lists(
        val id: Int?,
        val date: String?,
        val start_time: String?,
        val end_time: String?,
        val book_end_date: String?,
        val room: String?,
        val vpml: String?,
        val status: Int?,
        val booking_open: String?
    )

    }
}