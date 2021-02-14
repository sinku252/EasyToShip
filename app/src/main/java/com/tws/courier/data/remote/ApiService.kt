package com.tws.courier.data.remote

import com.google.gson.JsonObject
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.models.*
import com.tws.courier.domain.model.remote.response.BaseResponse

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {


    @POST("user_login")
    fun loginUser(@Body requestMap: Map<String, String>): Deferred<BaseResponse<User>>


    @POST("user_registration")
    fun register(@Body requestMap: Map<String, String>): Deferred<BaseResponse<User>>

    @POST("forgotpassword")
    fun forget(@Body requestMap: Map<String, String>): Deferred<BaseResponse<ForgotPasswordResponse>>

    @POST("my_address_list")
    fun addressList(@Body requestMap: Map<String, String>): Deferred<BaseResponse<List<Address>>>

    @POST("add_new_address")
    fun AddNewAddress(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Address>>

    @POST("get_dashdata")
    fun getDashData(@Body requestMap: Map<String, String>): Deferred<BaseResponse<List<String>>>

    @POST("get_order_list")
    fun getOrders(@Body requestMap: Map<String, String>): Deferred<BaseResponse<List<Order>>>

    @POST("get_general")
    fun getGeneral(@Body requestMap: Map<String, String>): Deferred<BaseResponse<User>>

    @POST("get_bank")
    fun getBank(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Bank>>


    @POST("home_page_sliders")
    fun getSlider(@Body requestMap: Map<String, String>): Deferred<BaseResponse<List<HomeSlider>>>

    @POST("add_general_details")
    fun addGeneralDetails(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>

    @POST("add_bank_details")
    fun addBankDetails(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>

    @POST("view_orderprice")
    fun viewOrderPrice(@Body requestMap: Map<String, String>): Deferred<BaseResponse<OrderPrice>>

    @POST("step1_country_validation")
    fun step1CountryValidation(@Body requestMap: Map<String, String>): Deferred<BaseResponse<String>>

    @POST("add_orderprices")
    fun placeOrder(@Body requestMap: Map<String, String>): Deferred<BaseResponse<OrderSuccess>>

    @POST("step4_validation")
    fun step4Validation(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>

    @POST("delete_myaddress")
    fun deleteAddress(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>

    @POST("update_password")
    fun updatePassword(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>


    @POST("get_order_calculation_details")
    fun getOrderCalculationDetails(@Body requestMap: Map<String, String>):  Deferred<BaseResponse<List<OrderCalculation>>>

    @POST("upDateFCMToken")
    fun upDateFCMToken(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>


    @POST("add_ticket")
    fun addTicket(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Any>>

    @POST("chats_add")
    fun sendMsg(@Body requestMap: Map<String, String>): Deferred<BaseResponse<Chat>>

    @POST("ticket_list")
    fun ticketList(@Body requestMap: Map<String, String>):Deferred<BaseResponse<List<Help>>>

    @POST("chats_list")
    fun chatList(@Body requestMap: Map<String, String>):Deferred<BaseResponse<List<Chat>>>

    @Multipart
    @POST("upload_community_images")
    fun updateUserProfile(
        @Part("user_id") user_id: RequestBody? = null,
        @Part profile_pic: MultipartBody.Part? = null
    ): Deferred<BaseResponse<User>>


    @GET("distancematrix/json") // origins/destinations:  LatLng as string
    fun getDistance(@Body requestMap: Map<String, String>): Deferred<BaseResponse<ResultDistanceMatrix>>



   /* @GET("distancematrix/json") // origins/destinations:  LatLng as string
    Call<ResultDistanceMatrix> getDistance(@Query("key") String key, @Query("origins") String origins, @Query("destinations") String destinations);
}*/

}