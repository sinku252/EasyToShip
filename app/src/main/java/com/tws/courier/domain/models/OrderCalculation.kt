package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class OrderCalculation(
    @SerializedName("area_type")
    var areaType: String,
    @SerializedName("cess_surcharge_percentage")
    var cessSurchargePercentage: String,
    @SerializedName("commission_entry_module_other_network")
    var commissionEntryModuleOtherNetwork: String,
    @SerializedName("commission_entry_module_self_network")
    var commissionEntryModuleSelfNetwork: String,
    @SerializedName("delivery_challan_price")
    var deliveryChallanPrice: String,
    @SerializedName("discount_entry_module_corporate_user")
    var discountEntryModuleCorporateUser: String,
    @SerializedName("discount_entry_module_others")
    var discountEntryModuleOthers: String,
    @SerializedName("discount_entry_module_self_network")
    var discountEntryModuleSelfNetwork: String,
    @SerializedName("first_distance_price")
    var firstDistancePrice: String,
    @SerializedName("first_distance_slab")
    var firstDistanceSlab: String,
    @SerializedName("first_porter_time_prize")
    var firstPorterTimePrize: String,
    @SerializedName("first_porter_time_slab")
    var firstPorterTimeSlab: String,
    @SerializedName("first_waiting_price")
    var firstWaitingPrice: String,
    @SerializedName("first_waiting_time")
    var firstWaitingTime: String,
    @SerializedName("first_weight_price")
    var firstWeightPrice: String,
    @SerializedName("first_weight_slab")
    var firstWeightSlab: String,
    @SerializedName("fuel_surcharge")
    var fuelSurcharge: String,
    @SerializedName("gst_percentage")
    var gstPercentage: String,
    @SerializedName("id")
    var id: String,
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
    @SerializedName("next_distance_price")
    var nextDistancePrice: String,
    @SerializedName("next_distance_slab")
    var nextDistanceSlab: String,
    @SerializedName("next_porter_time_prize")
    var nextPorterTimePrize: String,
    @SerializedName("next_porter_time_slab")
    var nextPorterTimeSlab: String,
    @SerializedName("next_waiting_price")
    var nextWaitingPrice: String,
    @SerializedName("next_waiting_time")
    var nextWaitingTime: String,
    @SerializedName("next_weight_price")
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
    @SerializedName("transmission_speed")
    var transmissionSpeed: String,
    @SerializedName("type")
    var type: String
)