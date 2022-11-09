package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentSubmissionListModel (
    @SerializedName("merchant_order_reference") val merchant_order_reference: String,
    @SerializedName("order_reference") val order_reference: String,
    @SerializedName("payment_histories_id") val payment_histories_id: String
)