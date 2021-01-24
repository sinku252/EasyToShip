package com.tws.courier.domain.models.remote_data.request

import com.google.gson.annotations.SerializedName

data class SendTicketRequest(
    @SerializedName("vrm")
    var vrm: String = "",
    @SerializedName("contravention")
    var offence: String = "",
    @SerializedName("note")
    var note: String = "",
    @SerializedName("pcnnumber")
    var pcnNumber: String = "",
    @SerializedName("observedFrom")
    var observedFrom: String = "",
    @SerializedName("usrid")
    var userid: Long = 0,
    @SerializedName("issuedate")
    var issueDate: String = "",
    @SerializedName("colour")
    var colour: String = "",
    @SerializedName("glat")
    var glat: String = "0",
    @SerializedName("glong")
    var glong: String = "0",
    @SerializedName("make")
    var make: String = "",
    @SerializedName("site")
    var site: String = "",
    @SerializedName("did")
    var deviceId: String = "",
    @SerializedName("observedTo")
    var observedTo: String = ""
)