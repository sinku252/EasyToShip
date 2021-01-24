package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class Help(
    @SerializedName("child_tickets")
    var childTickets: List<ChildTicket>,
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
    var status: Int,
    @SerializedName("subject")
    var subject: String,
    @SerializedName("user_id")
    var userId: String

)