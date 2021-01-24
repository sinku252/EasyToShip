package com.tws.courier.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReqCO(
    @SerializedName("area_type")
    var areaType: String,
    @SerializedName("awb")
    var awb: Long,
    @SerializedName("delivery_mobile_number")
    var deliveryMobileNumber: String,
    @SerializedName("delivery_status")
    var deliveryStatus: Int,
    @SerializedName("end_latitude")
    var endLatitude: String,
    @SerializedName("end_longitude")
    var endLongitude: String,
    @SerializedName("height")
    var height: String,
    @SerializedName("length")
    var length: String,
    @SerializedName("order_type")
    var orderType: String,
    @SerializedName("payment_mode")
    var paymentMode: String,
    @SerializedName("payment_status")
    var paymentStatus: String,
    @SerializedName("pickup_mobile_number")
    var pickupMobileNumber: String,
    @SerializedName("reference_id ")
    var referenceId: Long,
    @SerializedName("schedule")
    var schedule: String,
    @SerializedName("seller_type")
    var sellerType: String,
    @SerializedName("shipment_type")
    var shipmentType: String,
    @SerializedName("start_latitude")
    var startLatitude: String,
    @SerializedName("start_longitude")
    var startLongitude: String,
    @SerializedName("total_amount")
    var totalAmount: String,
    @SerializedName("total_delivery_challan")
    var totalDeliveryChallan: String,
    @SerializedName("total_gst_charge")
    var totalGstCharge: String,
    @SerializedName("total_insurance_charge")
    var totalInsuranceCharge: String,
    @SerializedName("total_night_charge")
    var totalNightCharge: Int,
    @SerializedName("total_other_charge")
    var totalOtherCharge: String,
    @SerializedName("total_peak_charge")
    var totalPeakCharge: String,
    @SerializedName("total_porter_charge")
    var totalPorterCharge: String,
    @SerializedName("transaction_id")
    var transactionId: Long,
    @SerializedName("transmission_speed")
    var transmissionSpeed: String,
    @SerializedName("transport_type")
    var transportType: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("vehicle_type")
    var vehicleType: String,
    @SerializedName("weight")
    var weight: String,
    @SerializedName("width")
    var width: String,

    @SerializedName("origin")
var origin: String,
@SerializedName("destination")
var destination: String
): Parcelable