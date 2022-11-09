package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentCreditInitiateDataModel(
    @SerializedName("redirect_url") val redirect_url: String,
    @SerializedName("paymenturl") val paymenturl: String,
    @SerializedName("formdata") val formdataModel: PaymentCreditInitiateFormDataModel
)