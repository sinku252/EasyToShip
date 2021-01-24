package com.tws.courier.domain.models.remote_data.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SyncDataResponseS07(
    @SerializedName("error") val error: Boolean = false,
    @SerializedName("data") val data: JsonObject,
    @SerializedName("transactionCode") val transactionCode: String,
    @SerializedName("numRows") val numRows: Int,
    @SerializedName("ErrorMsgForTest") val errorMsgForTest: String
)