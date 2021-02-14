package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class OrderPrice(
    @SerializedName("total_amount_price")
    var totalAmountPrice: String,
    @SerializedName("total_delivery_challan")
    var totalDeliveryChallan: String,
    @SerializedName("total_distance")
    var totalDistance: String,
    @SerializedName("total_distance_price")
    var totalDistancePrice: String,
    @SerializedName("total_fair")
    var totalFair: String,
    @SerializedName("total_gst_charge")
    var totalGstCharge: String,
    @SerializedName("total_insurance_charge")
    var totalInsuranceCharge: String,
    @SerializedName("total_other_charge")
    var totalOtherCharge: String,
    @SerializedName("total_peak_charge")
    var totalPeakCharge: String,
    @SerializedName("total_porter_charge")
    var totalPorterCharge: String,
    @SerializedName("total_reverse_trip")
    var totalReverseTrip: String,
    @SerializedName("total_weight_price_national")
    var totalWeightPriceNational: List<TotalWeightPrice>?,
    @SerializedName("total_wieght_price")
    var totalWieghtPrice: String,
    @SerializedName("total_weight_price_international")
    var totalWeightPriceInternational: List<TotalWeightPrice>?


 /*   @SerializedName("total_amount_price")
    var totalAmountPrice: String,
    @SerializedName("total_delivery_challan")
    var totalDeliveryChallan: String,
    @SerializedName("total_fair")
    var totalFair: String,
    @SerializedName("total_gst_charge")
    var totalGstCharge: String,
    @SerializedName("total_insurance_charge")
    var totalInsuranceCharge: String,
    @SerializedName("total_peak_charge")
    var totalPeakCharge: String,
    @SerializedName("total_porter_charge")
    var totalPorterCharge: String,
    @SerializedName("total_reverse_trip")
    var totalReverseTrip: String,
    @SerializedName("total_other_charge")
    var totalOtherCharge: String,
    @SerializedName("total_distance_price")
    var totalDistancePrice: String,
    @SerializedName("total_wieght_price")
    var totalWieghtPrice: String
*/

    /* @SerializedName("area_type")
    var areaType: String,
    @SerializedName("delivery_address")
    var deliveryAddress: String,
    @SerializedName("delivery_pincode")
    var deliveryPincode: String,
    @SerializedName("delivery_status")
    var deliveryStatus: Int,
    @SerializedName("height")
    var height: String,
    @SerializedName("is_delivery_charges")
    var isDeliveryCharges: String,
    @SerializedName("is_door_delivery")
    var isDoorDelivery: String,
    @SerializedName("is_insurance")
    var isInsurance: String,
    @SerializedName("is_porter")
    var isPorter: String,
    @SerializedName("is_reverse_trip")
    var isReverseTrip: String,
    @SerializedName("is_self_pickup")
    var isSelfPickup: String,
    @SerializedName("length")
    var length: String,
    @SerializedName("payment_mode")
    var paymentMode: String,
    @SerializedName("payment_status")
    var paymentStatus: String,
    @SerializedName("pickup_address")
    var pickupAddress: String,
    @SerializedName("pickup_pincode")
    var pickupPincode: String,
    @SerializedName("shipment_type")
    var shipmentType: String,
    @SerializedName("total_amount")
    var totalAmount: Int,
    @SerializedName("total_distance_price")
    var totalDistancePrice: Int,
    @SerializedName("total_weight_price")
    var totalWeightPrice: Int,
    @SerializedName("transaction_id")
    var transactionId: Long,
    @SerializedName("transmission_speed")
    var transmissionSpeed: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("vehicle_price")
    var vehiclePrice: VehiclePrice,
    @SerializedName("vehicle_type")
    var vehicleType: String,
    @SerializedName("weight")
    var weight: String,
    @SerializedName("width")
    var width: String*/
)

data class TotalWeightPrice(
    @SerializedName("type")
    var type: String,
    @SerializedName("value")
    var value: String
)
