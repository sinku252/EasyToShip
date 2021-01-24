package com.tws.courier.domain.models

import android.view.View
import com.google.gson.annotations.SerializedName

data class CheckBox(
    @SerializedName("position")
    var position:Int,
    @SerializedName("checked")
    var checked:Boolean

)
