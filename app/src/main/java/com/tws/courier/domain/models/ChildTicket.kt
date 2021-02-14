package com.tws.courier.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildTicket(
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("parent_id")
    var parentId: String,
    @SerializedName("seller_type")
    var sellerType: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("subject")
    var subject: String,
    @SerializedName("user_id")
    var userId: String
):Parcelable