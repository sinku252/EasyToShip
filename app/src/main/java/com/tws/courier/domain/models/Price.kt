package com.tws.courier.domain.models

import com.google.gson.annotations.SerializedName

data class Price
(
    @SerializedName("fws")
    val fws: String,

    @SerializedName("nws")
    val nws: String,

    @SerializedName("fwp")
    val fwp: String,

    @SerializedName("nwp")
    val nwp: String,

    @SerializedName("fds")
    val fds: String,

    @SerializedName("nds")
    val nds: String,

    @SerializedName("fdp")
    val fdp: String,

    @SerializedName("ndp")
    val ndp: String,

    @SerializedName("type")
    val type: VehicleType
)