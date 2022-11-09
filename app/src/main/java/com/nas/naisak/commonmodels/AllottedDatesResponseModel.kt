package com.nas.naisak.commonmodels

data class AllottedDatesResponseModel(
    val `data`: Data?,
    val message: String?,
    val status: Int?,
    val validation_errors: List<Any?>?
) {
    data class Data(
        val lists: ArrayList<String?>?
    )
}