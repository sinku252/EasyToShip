package com.tws.courier.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeSlider(
    @SerializedName("category_id")
    var categoryId: String,
    @SerializedName("created_by")
    var createdBy: String,
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("modified_by")
    var modifiedBy: String,
    @SerializedName("modified_date")
    var modifiedDate: String,
    @SerializedName("redirect_link")
    var redirectLink: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("type")
    var type: String
) : Parcelable