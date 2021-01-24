package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

 class CreateOrder(
    @SerializedName("area_type")
    var areaType: String="",
    @SerializedName("delivery_address")
    var deliveryAddress:  String="",
    @SerializedName("delivery_pincode")
    var deliveryPincode:  String="",
    @SerializedName("door_delivery")
    var doorDelivery:  String="0",
    @SerializedName("height")
    var height:  String="",
    @SerializedName("is_delivery_charges")
    var isDeliveryCharges: Int=0,
    @SerializedName("is_insurance")
    var isInsurance:   Int=0,
    @SerializedName("is_porter")
    var isPorter:  Int=0,
    @SerializedName("is_reverse_trip")
    var isReverseTrip:   Int=0,
    @SerializedName("length")
    var length:  String="",
    @SerializedName("payment_mode")
    var paymentMode:  String="",
    @SerializedName("payment_status")
    var paymentStatus:  String="",
    @SerializedName("pickup_address")
    var pickupAddress:  String="",
    @SerializedName("pickup_pincode")
    var pickupPincode:  String="",
    @SerializedName("self_pickup")
    var selfPickup:  String="0",
    @SerializedName("seller_type")
    var sellerType:  String="",
    @SerializedName("shipment_type")
    var shipmentType:  String="",
    @SerializedName("transmission_speed")
    var transmissionSpeed:  String="",
    @SerializedName("user_id")
    var userId:  String="",
    @SerializedName("vehicle_type")
    var vehicleType:  String="",
    @SerializedName("weight")
    var weight:   String="" ,
    @SerializedName("width")
    var width:  String="",

    @SerializedName("porter_count")
    var porterCount:  String="",

    @SerializedName("import")
    var import:  String="",
    @SerializedName("export")
    var export:  String="",

    @SerializedName("pickup_mobile_number")
    var pickupMobileNumber:  String="",
    @SerializedName("delivery_mobile_number")
    var deliveryMobileNumber:  String="",

    @SerializedName("deliveryType")
var deliveryType:  String=""

)