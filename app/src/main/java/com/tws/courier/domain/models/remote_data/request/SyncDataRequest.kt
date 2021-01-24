package com.tws.courier.domain.models.remote_data.request

import com.app.monrotv.domain.models.DeviceId
import com.google.gson.annotations.SerializedName


//tansactionId:0
//cid:56
//uid:401
//transactionCode:S03
//data:{"did" : "0DE5116F-BE4C-4A0C-8024-BEFB999D0906"}

data class SyncDataRequest(
    @SerializedName("tansactionId") val tansactionId: String,
    @SerializedName("cid") val cid: Long,
    @SerializedName("uid") val uid: Long,
    @SerializedName("transactionCode") val transactionCode: String,
    @SerializedName("data") val data: DeviceId
)