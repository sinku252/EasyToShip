package com.tws.courier.domain.models


import com.app.monrotv.ui.dashboard.generic.AddressListItem
import com.google.gson.annotations.SerializedName
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.domain.universal_adapter.RecyclerItem

data class Address(
    @SerializedName("address")
    var address: String,
    @SerializedName("area")
    var area: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("create_at")
    var createAt: String,
    @SerializedName("house_no")
    var houseNo: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("landmark")
    var landmark: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("phone_no")
    var phoneNo: String,
    @SerializedName("pincode")
    var pincode: String,
    @SerializedName("seller_type")
    var sellerType: String,
    @SerializedName("state")
    var state: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("address_type")
    var addressType: String,

    @SerializedName("isSelect")
    var isSelect: Boolean
): AddressListItem()

