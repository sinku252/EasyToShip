package com.tws.courier.domain.models


import com.app.monrotv.ui.dashboard.generic.AddressListItem
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("address_id")
    var addressId: String,
    @SerializedName("agent_id")
    var agentId: String,
    @SerializedName("app_token_id")
    var appTokenId: String,
    @SerializedName("area_type")
    var areaType: String,
    @SerializedName("awb")
    var awb: String,
    @SerializedName("browser")
    var browser: String,
    @SerializedName("browser_version")
    var browserVersion: String,
    @SerializedName("coupon_id")
    var couponId: String,
    @SerializedName("created_by")
    var createdBy: String,
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("delivery_address")
    var deliveryAddress: String,
    @SerializedName("delivery_pincode")
    var deliveryPincode: String,
    @SerializedName("delivery_status")
    var deliveryStatus: String,
    @SerializedName("destination")
    var destination: String,
    @SerializedName("device_type")
    var deviceType: String,
    @SerializedName("document_type")
    var documentType: String,
    @SerializedName("gst_amount")
    var gstAmount: String,
    @SerializedName("height")
    var height: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("import_export_level")
    var importExportLevel: String,
    @SerializedName("invoice_url")
    var invoiceUrl: String,
    @SerializedName("is_delivery_charges")
    var isDeliveryCharges: String,
    @SerializedName("is_door_delivery")
    var isDoorDelivery: String,
    @SerializedName("is_insurance")
    var isInsurance: String,
    @SerializedName("is_porter")
    var isPorter: String,
    @SerializedName("is_reverse_trip")
    var isReverseTrip: String,
    @SerializedName("is_self_pickup")
    var isSelfPickup: String,
    @SerializedName("length")
    var length: String,
    @SerializedName("mobile_device")
    var mobileDevice: String,
    @SerializedName("modified_by")
    var modifiedBy: String,
    @SerializedName("modified_date")
    var modifiedDate: String,
    @SerializedName("order_group_id")
    var orderGroupId: String,
    @SerializedName("os")
    var os: String,
    @SerializedName("payment_mode")
    var paymentMode: String,
    @SerializedName("payment_status")
    var paymentStatus: String,
    @SerializedName("pickup_address")
    var pickupAddress: String,
    @SerializedName("pickup_pincode")
    var pickupPincode: String,
    @SerializedName("product_id")
    var productId: String,
    @SerializedName("received_date")
    var receivedDate: String,
    @SerializedName("reference_id")
    var referenceId: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("rider_id")
    var riderId: String,
    @SerializedName("seller_type")
    var sellerType: String,
    @SerializedName("shipment_type")
    var shipmentType: String,
    @SerializedName("source")
    var source: String,
    @SerializedName("total_addition")
    var totalAddition: String,
    @SerializedName("total_amount")
    var totalAmount: String,
    @SerializedName("total_discount")
    var totalDiscount: String,
    @SerializedName("total_paid")
    var totalPaid: String,
    @SerializedName("total_quantity")
    var totalQuantity: String,
    @SerializedName("transaction_id")
    var transactionId: String,
    @SerializedName("transaction_msg")
    var transactionMsg: String,
    @SerializedName("transmission_speed")
    var transmissionSpeed: String,
    @SerializedName("transport_type")
    var transportType: String,
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("user_ip")
    var userIp: String,
    @SerializedName("vechical_id")
    var vechicalId: String,
    @SerializedName("vehicle_type")
    var vehicleType: String,
    @SerializedName("web_token_id")
    var webTokenId: String,
    @SerializedName("weight")
    var weight: String,
    @SerializedName("width")
    var width: String
)