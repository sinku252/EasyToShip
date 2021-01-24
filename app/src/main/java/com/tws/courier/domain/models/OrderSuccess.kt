package com.tws.courier.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderSuccess(
    @SerializedName("address_id")
    var addressId: String,
    @SerializedName("agent_id")
    var agentId: String,
    @SerializedName("app_token_id")
    var appTokenId: String,
    @SerializedName("area_type")
    var areaType: String,
    @SerializedName("awb")
    var awb: String,
    @SerializedName("browser")
    var browser: String,
    @SerializedName("browser_version")
    var browserVersion: String,
    @SerializedName("created_by")
    var createdBy: String,
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("delivery_address")
    var deliveryAddress: String,
    @SerializedName("delivery_mobile_number")
    var deliveryMobileNumber: String,
    @SerializedName("delivery_pincode")
    var deliveryPincode: String,
    @SerializedName("delivery_status")
    var deliveryStatus: String,
    @SerializedName("destination")
    var destination: String,
    @SerializedName("device_type")
    var deviceType: String,
    @SerializedName("document_type")
    var documentType: String,
    @SerializedName("end_latitude")
    var endLatitude: String,
    @SerializedName("end_longitude")
    var endLongitude: String,
    @SerializedName("height")
    var height: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("import_export_level")
    var importExportLevel: String,
    @SerializedName("invoice_url")
    var invoiceUrl: String,
    @SerializedName("is_door_delivery")
    var isDoorDelivery: String,
    @SerializedName("is_self_pickup")
    var isSelfPickup: String,
    @SerializedName("length")
    var length: String,
    @SerializedName("mobile_device")
    var mobileDevice: String,
    @SerializedName("modified_by")
    var modifiedBy: String,
    @SerializedName("modified_date")
    var modifiedDate: String,
    @SerializedName("order_type")
    var orderType: String,
    @SerializedName("os")
    var os: String,
    @SerializedName("payment_mode")
    var paymentMode: String,
    @SerializedName("payment_status")
    var paymentStatus: String,
    @SerializedName("pickup_mobile_number")
    var pickupMobileNumber: String,
    @SerializedName("product_id")
    var productId: String,
    @SerializedName("received_date")
    var receivedDate: String,
    @SerializedName("reference_id")
    var referenceId: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("reqCO")
    var reqCO: ReqCO,
    @SerializedName("rider_id")
    var riderId: String,
    @SerializedName("schedule")
    var schedule: String,
    @SerializedName("seller_type")
    var sellerType: String,
    @SerializedName("shipment_type")
    var shipmentType: String,
    @SerializedName("source")
    var source: String,
    @SerializedName("start_latitude")
    var startLatitude: String,
    @SerializedName("start_longitude")
    var startLongitude: String,
    @SerializedName("total_addition")
    var totalAddition: String,
    @SerializedName("total_amount")
    var totalAmount: String,
    @SerializedName("total_delivery_challan")
    var totalDeliveryChallan: String,
    @SerializedName("total_discount")
    var totalDiscount: String,
    @SerializedName("total_distance_price")
    var totalDistancePrice: String,
    @SerializedName("total_gst_charge")
    var totalGstCharge: String,
    @SerializedName("total_insurance_charge")
    var totalInsuranceCharge: String,
    @SerializedName("total_night_charge")
    var totalNightCharge: String,
    @SerializedName("total_other_charge")
    var totalOtherCharge: String,
    @SerializedName("total_paid")
    var totalPaid: String,
    @SerializedName("total_peak_charge")
    var totalPeakCharge: String,
    @SerializedName("total_porter_charge")
    var totalPorterCharge: String,
    @SerializedName("total_reverse_trip")
    var totalReverseTrip: String,
    @SerializedName("total_weight_price")
    var totalWeightPrice: String,
    @SerializedName("transaction_id")
    var transactionId: String,
    @SerializedName("transaction_msg")
    var transactionMsg: String,
    @SerializedName("transmission_speed")
    var transmissionSpeed: String,
    @SerializedName("transport_type")
    var transportType: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("user_ip")
    var userIp: String,
    @SerializedName("vechical_id")
    var vechicalId: String,
    @SerializedName("vehicle_type")
    var vehicleType: String,
    @SerializedName("wallet")
    var wallet: String,
    @SerializedName("web_token_id")
    var webTokenId: String,
    @SerializedName("weight")
    var weight: String,
    @SerializedName("width")
    var width: String
): Parcelable