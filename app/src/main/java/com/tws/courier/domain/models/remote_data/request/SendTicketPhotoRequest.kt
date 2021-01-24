package com.tws.courier.domain.models.remote_data.request

import com.google.gson.annotations.SerializedName

data class SendTicketPhotoRequest(

//    "pcnnumber" : "99728000233",
//"did" : "0000-DSDEVICE-TEST-0000",
//"photo" : "iVBORw0KGgoAAAANSUhEUgAA"


    @SerializedName("pcnnumber")
    var pcnNumber: String = "",
    @SerializedName("did")
    var deviceId: String = "",
    @SerializedName("photo")
    var photo: String = ""
)