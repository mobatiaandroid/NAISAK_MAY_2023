package com.nas.naisak.activity.cca.model

data class CCAReviewResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val cca_reviews: ArrayList<CcaReview?>?
    ) {
        data class CcaReview(
            val choice1: Choice1?,
            val choice2: Choice2?,
            val day: String?
        ) {
            data class Choice1(
                val absentDays: ArrayList<String?>?,
                val attending_status: String?,
                val cca_details_id: Int?,
                val cca_item_end_time: String?,
                val cca_item_name: String?,
                val cca_item_start_time: String?,
                val cca_venue: String?,
                val cca_item_description: String?,
                val day: String?,
                val isAttendee: String?,
                val presentDays: ArrayList<String?>?,
                val upcomingDays: ArrayList<String?>?
            )

            class Choice2(
                val absentDays: ArrayList<String?>?,
                val attending_status: String?,
                val cca_details_id: Int?,
                val cca_item_end_time: String?,
                val cca_item_name: String?,
                val cca_venue: String?,
                val cca_item_description: String?,
                val cca_item_start_time: String?,
                val day: String?,
                val isAttendee: String?,
                val presentDays: ArrayList<String?>?,
                val upcomingDays: ArrayList<String?>?
            )
        }
    }
}