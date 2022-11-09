package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentGatewayDataModel (
    @SerializedName("lists") val lists: PaymentGatewayListModel
)