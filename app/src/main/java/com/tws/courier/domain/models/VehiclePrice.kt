package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class VehiclePrice(
    @SerializedName("cess_surcharge_percentage")
    var cessSurchargePercentage: String,
    @SerializedName("delivery_challan_Price")
    var deliveryChallanPrice: String,
    @SerializedName("first_distance_Price")
    var firstDistancePrice: String,
    @SerializedName("first_distance_Slab")
    var firstDistanceSlab: String,
    @SerializedName("first_porter_time_prize")
    var firstPorterTimePrize: String,
    @SerializedName("first_porter_time_slab")
    var firstPorterTimeSlab: String,
    @SerializedName("first_waiting_price")
    var firstWaitingPrice: String,
    @SerializedName("first_waiting_time")
    var firstWaitingTime: String,
    @SerializedName("first_weight_Price")
    var firstWeightPrice: String,
    @SerializedName("first_weight_slab")
    var firstWeightSlab: String,
    @SerializedName("gst_percentage")
    var gstPercentage: String,
    @SerializedName("insurance_charge_percentage")
    var insuranceChargePercentage: String,
    @SerializedName("max_distance")
    var maxDistance: String,
    @SerializedName("max_weight_limit")
    var maxWeightLimit: String,
    @SerializedName("min_distance")
    var minDistance: String,
    @SerializedName("min_weight_limit")
    var minWeightLimit: String,
    @SerializedName("next_distance_Price")
    var nextDistancePrice: String,
    @SerializedName("next_distance_slab")
    var nextDistanceSlab: String,
    @SerializedName("next_porter_time_prize")
    var nextPorterTimePrize: String,
    @SerializedName("next_porter_timeslab")
    var nextPorterTimeslab: String,
    @SerializedName("next_waiting_Price")
    var nextWaitingPrice: String,
    @SerializedName("next_waiting_time")
    var nextWaitingTime: String,
    @SerializedName("next_weight_Price")
    var nextWeightPrice: String,
    @SerializedName("next_weight_slab")
    var nextWeightSlab: String,
    @SerializedName("night_charge_percentage")
    var nightChargePercentage: String,
    @SerializedName("other_surcharge_percentage")
    var otherSurchargePercentage: String,
    @SerializedName("peak_surcharge_percentage")
    var peakSurchargePercentage: String,
    @SerializedName("reverse_trip_percentage")
    var reverseTripPercentage: String,
    @SerializedName("Type")
    var type: String
)