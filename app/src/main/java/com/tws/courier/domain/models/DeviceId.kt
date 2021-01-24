package com.app.monrotv.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceId(
    @SerializedName("did") val deviceId: String
) : Parcelable