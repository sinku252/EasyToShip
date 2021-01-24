package com.tws.courier.domain.models

import com.google.gson.annotations.SerializedName

data class DashBoard(
    @SerializedName("type")
    var type: String,
    @SerializedName("count")
    var count: String

)
