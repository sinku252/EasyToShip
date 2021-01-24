package com.tws.courier.domain.models.remote_data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error") @Expose val error: Boolean = false,
    @SerializedName("errormsg") @Expose val errorMessage: String,
    @SerializedName("cid") @Expose val cid: Long,
    @SerializedName("uid") @Expose val uid: Long,
    @SerializedName("badgeId") @Expose val badgeId: String,
    @SerializedName("orgName") @Expose val orgName: String
)