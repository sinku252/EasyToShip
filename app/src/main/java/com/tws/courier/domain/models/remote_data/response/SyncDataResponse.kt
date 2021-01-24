package com.tws.courier.domain.models.remote_data.response

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class SyncDataResponse(
    @SerializedName("error") val error: Boolean = false,
    @SerializedName("data") val data: JsonArray,
    @SerializedName("transactionCode") val transactionCode: String,
    @SerializedName("numRows") val numRows: Int,
    @SerializedName("ErrorMsgForTest") val errorMsgForTest: String
)