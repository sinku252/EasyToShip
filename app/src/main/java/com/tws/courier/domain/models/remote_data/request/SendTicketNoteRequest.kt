package com.tws.courier.domain.models.remote_data.request

import com.google.gson.annotations.SerializedName

data class SendTicketNoteRequest(

//    "did" : "0000-DSDEVICE-TEST-0000",
//"note" : "Sdfsadfsadfasdfasdfasdfasdfs",
//"pcnnumber" : "99728000233"

    @SerializedName("pcnnumber")
    var pcnNumber: String = "",
    @SerializedName("did")
    var deviceId: String = "",
    @SerializedName("note")
    var note: String = ""
)