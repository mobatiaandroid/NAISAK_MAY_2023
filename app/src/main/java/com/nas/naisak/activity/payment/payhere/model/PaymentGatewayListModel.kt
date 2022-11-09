package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentGatewayListModel (
    @SerializedName("status") val status: Boolean,
    @SerializedName("payment_url") val payment_url: String
)