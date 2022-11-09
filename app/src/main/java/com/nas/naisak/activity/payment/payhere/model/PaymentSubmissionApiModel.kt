package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentSubmissionApiModel (
@SerializedName("reference") val reference: String,
@SerializedName("merchant_order_reference") val merchant_order_reference: String
)