package com.tws.courier.domain.models.remote_data.request

import com.google.gson.annotations.SerializedName
import com.tws.courier.AppManager


//version:1.0
//username:AshuM
//password:Dots@123*
//did:0DE5116F-BE4C-4A0C-8024-BEFB999D0906

data class LoginRequest(
    @SerializedName("username")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("did")
    val deviceId: String,
    @SerializedName("version")
    val version: String = AppManager.API_VERSION
)