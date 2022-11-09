package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentSubmissionDataModel (

    @SerializedName("lists") val lists: PaymentSubmissionListModel
        )