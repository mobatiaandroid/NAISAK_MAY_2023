package com.nas.naisak.activity.cca.model

data class CCAListResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val lists: List<Lists?>?
    ) {
        data class Lists(
            val cca_days_id: Int?,
            val details: ArrayList<Detail?>?,
            val from_date: String?,
            val isAttendee: String?,
            val isSubmissiondateOver: String?,
            val status: Int?,
            val submission_dateTime: String?,
            val title: String?,
            val to_date: String?
        ) {
            data class Detail(
                val choice1: ArrayList<Choice1?>?,
                val choice2: ArrayList<Choice2?>?,
                val day: String?
            ) {
                data class Choice1(
                    val attending_status: String?,
                    val cca_details_id: Int?,
                    val cca_item_end_time: String?,
                    val cca_item_name: String?,
                    val cca_item_start_time: String?,
                    val day: String?,
                    val isAttendee: String?,
                    val venue: String?
                )

                data class Choice2(
                    val attending_status: String?,
                    val cca_details_id: Int?,
                    val cca_item_end_time: String?,
                    val cca_item_name: String?,
                    val cca_item_start_time: String?,
                    val day: String?,
                    val isAttendee: String?,
                    val venue: String?
                )
            }
        }
    }
}