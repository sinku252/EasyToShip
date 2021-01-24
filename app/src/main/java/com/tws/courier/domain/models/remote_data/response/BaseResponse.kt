package com.tws.courier.domain.model.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

   /* @SerializedName("status") @Expose val status: Boolean,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("code") @Expose val code: Int,
    @SerializedName("data") @Expose val data: T*/
    @SerializedName("response") @Expose val response: Response<T>

)
data class Response<T>(
    @SerializedName("status") @Expose val status: String,
    @SerializedName("message") @Expose val message: String,

    @SerializedName("data") @Expose val data: T
)
