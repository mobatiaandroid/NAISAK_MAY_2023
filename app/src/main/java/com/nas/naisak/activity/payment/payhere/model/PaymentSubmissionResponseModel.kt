package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentSubmissionResponseModel (
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("validation_errors") val validationErrorArray: List<String>,
    @SerializedName("data") val data: PaymentSubmissionDataModel
)