package com.tws.courier.domain.models.remote_data.response


import com.google.gson.annotations.SerializedName

data class PurchaseResponse(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("ends_at")
    val endsAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("stripe_id")
    val stripeId: String,
    @SerializedName("stripe_plan")
    val stripePlan: Long,
    @SerializedName("subscription_from")
    val subscriptionFrom: String,
    @SerializedName("subscription_to")
    val subscriptionTo: String,
    @SerializedName("trial_ends_at")
    val trialEndsAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Long
)