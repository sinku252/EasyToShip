package com.tws.courier.domain.models

import android.view.View
import com.google.gson.annotations.SerializedName

data class Demo(
    @SerializedName("view")
    var view: View,
    @SerializedName("type")
    var type:Int

)
