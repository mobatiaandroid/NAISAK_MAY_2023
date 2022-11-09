package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentReceiptApiModel (
    @SerializedName("payment_id") val payment_id: String
)