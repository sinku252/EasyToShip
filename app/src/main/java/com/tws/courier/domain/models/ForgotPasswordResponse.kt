package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("otp")
    var otp: Int,
    @SerializedName("user_id")
    var userId: String
)