package com.nas.naisak.activity.payment.payhere.model

import com.google.gson.annotations.SerializedName

class PaymentCreditInitiateFormDataModel (
    @SerializedName("bill_to_forename") val bill_to_forename: String,
    @SerializedName("bill_to_surname") val bill_to_surname: String,
    @SerializedName("bill_to_email") val bill_to_email: String,
    @SerializedName("bill_to_address_line1") val bill_to_address_line1: String,
    @SerializedName("bill_to_address_city") val bill_to_address_city: String,
    @SerializedName("bill_to_address_postal_code") val bill_to_address_postal_code: String,
    @SerializedName("bill_to_address_state") val bill_to_address_state: String,
    @SerializedName("bill_to_address_country") val bill_to_address_country: String,
    @SerializedName("reference_number") val reference_number: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("transaction_uuid") val transaction_uuid: String,
    @SerializedName("access_key") val access_key: String,
    @SerializedName("profile_id") val profile_id: String,
    @SerializedName("locale") val locale: String,
    @SerializedName("transaction_type") val transaction_type: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("signed_field_names") val signed_field_names: String,
    @SerializedName("unsigned_field_names") val unsigned_field_names: String,
    @SerializedName("device_fingerprint_id") val device_fingerprint_id: String,
    @SerializedName("customer_ip_address") val customer_ip_address: String,
    @SerializedName("merchantID") val merchantID: String,
    @SerializedName("merchantReferenceCode") val merchantReferenceCode: String,
    @SerializedName("signed_date_time") val signed_date_time: String,
    @SerializedName("signature") val signature: String
)