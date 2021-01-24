package com.tws.courier.domain.models


import com.google.gson.annotations.SerializedName

data class Bank(
    @SerializedName("about")
    var about: String,
    @SerializedName("account_holder_name")
    var accountHolderName: String,
    @SerializedName("account_no")
    var accountNo: String,
    @SerializedName("account_type")
    var accountType: String,
    @SerializedName("address_id")
    var addressId: String,
    @SerializedName("affiliate_id")
    var affiliateId: String,
    @SerializedName("agent_id")
    var agentId: String,
    @SerializedName("bank_name")
    var bankName: String,
    @SerializedName("beneficiary_name")
    var beneficiaryName: String,
    @SerializedName("branch_city")
    var branchCity: String,
    @SerializedName("branch_name")
    var branchName: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("comment")
    var comment: String,
    @SerializedName("compString_name")
    var compStringName: String,
    @SerializedName("confirm_accountno")
    var confirmAccountno: String,
    @SerializedName("created_by")
    var createdBy: String,
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("dob")
    var dob: String,
    @SerializedName("doc_back_image")
    var docBackImage: String,
    @SerializedName("doc_front_image")
    var docFrontImage: String,
    @SerializedName("doc_no")
    var docNo: String,
    @SerializedName("doc_type")
    var docType: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("email_verified")
    var emailVerified: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("google_pay")
    var googlePay: String,
    @SerializedName("gst_no")
    var gstNo: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("ifsc_code")
    var ifscCode: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("last_login_date")
    var lastLoginDate: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("license_no")
    var licenseNo: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("mobile_device")
    var mobileDevice: String,
    @SerializedName("mobile_verified")
    var mobileVerified: String,
    @SerializedName("modified_by")
    var modifiedBy: String,
    @SerializedName("modified_date")
    var modifiedDate: String,
    @SerializedName("order_count")
    var orderCount: String,
    @SerializedName("os")
    var os: String,
    @SerializedName("pan_no")
    var panNo: String,
    @SerializedName("parent_id")
    var parentId: String,
    @SerializedName("passbook_image")
    var passbookImage: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("paytm")
    var paytm: String,
    @SerializedName("phonePe")
    var phonePe: String,
    @SerializedName("pincode")
    var pincode: String,
    @SerializedName("ref_id")
    var refId: String,
    @SerializedName("role_id")
    var roleId: String,
    @SerializedName("seller_type")
    var sellerType: String,
    @SerializedName("slug")
    var slug: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("total_earn")
    var totalEarn: String,
    @SerializedName("total_paid")
    var totalPaid: String,
    @SerializedName("total_rider")
    var totalRider: String,
    @SerializedName("total_vehicle")
    var totalVehicle: String,
    @SerializedName("transport_no")
    var transportNo: String,
    @SerializedName("user_ip")
    var userIp: String,
    @SerializedName("user_type")
    var userType: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("wallet_amount")
    var walletAmount: String
)