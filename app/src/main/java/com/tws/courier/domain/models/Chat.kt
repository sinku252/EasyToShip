package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("id")
    var id: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("seller_type")
    var sellerType: String
)