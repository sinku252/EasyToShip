package com.tws.courier.ui.create_shipment

import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.base.BaseFragment
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.*
import com.tws.courier.domain.universal_adapter.RecyclerItem
import com.tws.courier.domain.utils.getPrice
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class CreateShipmentViewModel : BaseViewModel()
{
   // var llShipmentArea = ObservableBoolean()
    //var llShipmentArea: ObservableBoolean = ObservableBoolean(false)
   val onCreateOrder = MutableLiveData<SingleActionEvent<CreateOrder>>()
    val llShipmentArea = MutableLiveData<SingleActionEvent<Demo>>()
    val llShipmentVhicleType = MutableLiveData<SingleActionEvent<Demo>>()
    val llShipmentAddressDimension = MutableLiveData<SingleActionEvent<Demo>>()
    val llShipmentAdditionalDetails = MutableLiveData<SingleActionEvent<Demo>>()
    val changeInWeight = MutableLiveData<SingleActionEvent<String>>()
    val navigateToAddress = MutableLiveData<SingleActionEvent<Boolean>>()
    val checkVelidation = MutableLiveData<SingleActionEvent<Boolean>>()
    val onOrderPriceSuccessful = MutableLiveData<SingleActionEvent<OrderPrice>>()
    val onOrderSuccessful = MutableLiveData<SingleActionEvent<OrderSuccess>>()
    val onAddressListReceived = MutableLiveData<SingleActionEvent<List<Address>>>()

    val onPorterSelect = MutableLiveData<SingleActionEvent<Boolean>>()


    var radio_checked = MutableLiveData<Int>()
    init{
        radio_checked.postValue(1)//def value
    }


   // val onAddressListReceived = MutableLiveData<List<Address>>()

    init {
     //   loadAddressData()
    }

    //val createOrder = MutableLiveData<Order>()



    var weight: String = "8"
    var password: String = ""


    fun showLayoutsByType(view: View, type: Int) {
       // Log.e("Fafasf",""+view.id)
        var demo=Demo(view, type)
        if(type==1)
        {
            llShipmentArea.value = SingleActionEvent(demo)
        }
        else if(type==2)
        {
            llShipmentVhicleType.value = SingleActionEvent(demo)
        }
        else if(type==3)
        {
            llShipmentAddressDimension.value = SingleActionEvent(demo)
        }
        else if(type==4)
        {
            llShipmentAdditionalDetails.value = SingleActionEvent(demo)
        }


      /*  llShipmentArea.set(true)
        view.visibleOrGone=false*/

    }

    fun changeWeight(createOrder: CreateOrder, type: Int)
    {
        var weightInt=createOrder.weight.toInt()
        if(type==1)
        {
            weightInt=weightInt+1;
        }
        else
        {
            if(weightInt>0)
            {
                weightInt=weightInt-1;
            }
        }
        createOrder.weight=weightInt.toString()
        changeInWeight.value = SingleActionEvent(createOrder.weight)

    }

    fun addAddress(value:Boolean) {
        closeKeyBoard()
        Log.e("fasf14",""+value)
        navigateToAddress.value = SingleActionEvent(value)
    }

    fun checkOrder(createOrder: CreateOrder)
    {
        closeKeyBoard()
        var isError:Boolean = false;
        if (TextUtils.isEmpty(createOrder.height.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_height_empty)
                )
            )
            isError=true
            return
        }
        if (TextUtils.isEmpty(createOrder.width.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_width_empty)
                )
            )
            isError=true
            return
        }
        if (TextUtils.isEmpty(createOrder.length.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_length_empty)
                )
            )
            isError=true
            return
        }
        if (TextUtils.isEmpty(createOrder.weight.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_weight_empty)
                )
            )
            isError=true
            return
        }
        if (createOrder.selfPickup.trim().equals("0") && createOrder.doorDelivery.trim().equals("0")) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_delivery_empty)
                )
            )
            isError=true
            return
        }
        if (TextUtils.isEmpty(createOrder.pickupPincode.trim()))  {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_pickup_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(createOrder.deliveryPincode.trim()))  {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_delivery_picode_empty)
                )
            )
            isError=true
            return
        }
        if (TextUtils.isEmpty(createOrder.pickupAddress.trim()))  {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_pickup_address_empty)
                )
            )
            isError=true
            return
        }

        Log.e("fafsa",isError.toString())


        val map = HashMap<String, String>()

        map["method_name"]="step4_validation"
        map["user_id"]=  AppManager.getUser()?.id ?: ""
        map["seller_type"]="customer"
        map["shipment_type"]=createOrder?.shipmentType
        map["area_type"]=createOrder?.areaType
        map["vehicle_type"]=createOrder?.vehicleType
        map["pickup_pincode"]=createOrder?.pickupPincode
        map["pickup_address"]=createOrder?.pickupAddress
        map["delivery_pincode"]=createOrder?.deliveryPincode
        map["delivery_address"]=createOrder?.deliveryAddress
        map["height"]=createOrder?.height
        map["width"]=createOrder?.width
        map["length"]=createOrder?.length
        map["weight"]=createOrder?.weight

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.apiService.step4Validation(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.STEP4_VALIDATION
                        override val repoListener: RepoListener
                            get() = this@CreateShipmentViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        //onOrderPriceSuccessful.value = SingleActionEvent(this.response.data)
                        checkVelidation.value = SingleActionEvent(false)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }

    }

    fun getOrderPrice(createOrder: CreateOrder)
    {
        closeKeyBoard()

        if (createOrder.isPorter==1)  {
            if(TextUtils.isEmpty(createOrder.porterCount))
            {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_porter_empty)
                    )
                )
                return
            }
        }

        val map = HashMap<String, String>()

        map["method_name"]="view_orderprice"
        map["user_id"]=  AppManager.getUser()?.id ?: ""
        map["seller_type"]="customer"
        map["shipment_type"]=createOrder?.shipmentType
        map["area_type"]=createOrder?.areaType
        map["vehicle_type"]=createOrder?.vehicleType
        map["pickup_pincode"]=createOrder?.pickupPincode
        map["pickup_address"]=createOrder?.pickupAddress
        map["delivery_pincode"]=createOrder?.deliveryPincode
        map["delivery_address"]=createOrder?.deliveryAddress
        map["height"]=createOrder?.height
        map["width"]=createOrder?.width
        map["length"]=createOrder?.length
        map["weight"]=createOrder?.weight
        map["door_delivery"]=createOrder?.doorDelivery
        map["self_pickup"]=createOrder?.selfPickup
        map["payment_mode"]=createOrder.paymentMode
        map["payment_status"]="0"
        map["is_insurance"]=createOrder?.isInsurance.toString()
        map["is_porter"]=createOrder?.isPorter.toString()
        map["is_reverse_trip"]=createOrder?.isReverseTrip.toString()
        map["is_delivery_charges"]=createOrder.isDeliveryCharges.toString()
        map["transmission_speed"]=createOrder?.transmissionSpeed
        map["porter_count"]=createOrder?.porterCount





        Log.e("fafsa",map.toString())

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<OrderPrice>> {
                        override val deferred: Deferred<BaseResponse<OrderPrice>>
                            get() = ApiClient.apiService.viewOrderPrice(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.VIEW_ORDER_PRICE
                        override val repoListener: RepoListener
                            get() = this@CreateShipmentViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        onOrderPriceSuccessful.value = SingleActionEvent(this.response.data)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun placeOrder(createOrder: CreateOrder,orderPrice: OrderPrice)
    {
        closeKeyBoard()

        if (TextUtils.isEmpty(createOrder.paymentMode.trim()))  {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_payment_mode_empty)
                )
            )
            return
        }

        if (TextUtils.isEmpty(AppManager.getUser()?.wallet_amount))  {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_wallet_empty)
                )
            )
            return
        }

        if (createOrder.isPorter==1)  {
            if(TextUtils.isEmpty(createOrder.porterCount))
            {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_porter_empty)
                    )
                )
                return
            }
        }

        val map = HashMap<String, String>()

        map["method_name"]="add_orderprices"
        map["user_id"]=  AppManager.getUser()?.id ?: ""
        map["seller_type"]="customer"
        map["shipment_type"]=createOrder?.shipmentType
        map["area_type"]=createOrder?.areaType
        map["vehicle_type"]=createOrder?.vehicleType
        map["pickup_pincode"]=createOrder?.pickupPincode
        map["pickup_address"]=createOrder?.pickupAddress
        map["delivery_pincode"]=createOrder?.deliveryPincode
        map["delivery_address"]=createOrder?.deliveryAddress
        map["height"]=createOrder?.height
        map["width"]=createOrder?.width
        map["length"]=createOrder?.length
        map["weight"]=createOrder?.weight
        map["door_delivery"]=createOrder?.doorDelivery
        map["self_pickup"]=createOrder?.selfPickup
        map["payment_mode"]=createOrder.paymentMode
        map["payment_status"]="0"
        map["is_insurance"]=createOrder?.isInsurance.toString()
        map["is_porter"]=createOrder?.isPorter.toString()
        map["is_reverse_trip"]=createOrder?.isReverseTrip.toString()
        map["is_delivery_charges"]=createOrder.isDeliveryCharges.toString()
        map["transmission_speed"]=createOrder?.transmissionSpeed
        map["porter_count"]=createOrder?.porterCount

        map["total_insurance_charge"]=orderPrice?.totalInsuranceCharge
        map["total_porter_charge"]=orderPrice?.totalPorterCharge
        map["total_reverse_trip"]=orderPrice?.totalReverseTrip
        map["total_delivery_challan"]=orderPrice?.totalDeliveryChallan
        map["total_gst_charge"]=orderPrice?.totalGstCharge
        map["total_peak_charge"]=orderPrice?.totalPeakCharge
        map["total_amount_price"]=orderPrice?.totalAmountPrice
        map["total_other_charge"]=orderPrice?.totalOtherCharge



        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<OrderSuccess>> {
                        override val deferred: Deferred<BaseResponse<OrderSuccess>>
                            get() = ApiClient.apiService.placeOrder(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.PLACE_ORDER
                        override val repoListener: RepoListener
                            get() = this@CreateShipmentViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        onOrderSuccessful.value = SingleActionEvent(this.response.data)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }



        fun onTypeChecked(createOrder: CreateOrder,checked: Boolean, i: Int) {

        if(i==1)
        {
            if (checked) {
                createOrder.isInsurance=1
            }
            else {
                // if it is a uncheck. set type to unknown
                createOrder.isInsurance=0
            }
        }
        else if(i==2)
        {
            if (checked) {
                createOrder.isPorter=1
                onPorterSelect.value = SingleActionEvent(true)
            }
            else {
                // if it is a uncheck. set type to unknown
                createOrder.isPorter=0
                onPorterSelect.value = SingleActionEvent(false)
            }
        }
        else if(i==3)
        {
            if (checked) {
                createOrder.isReverseTrip=1
            }
            else {
                // if it is a uncheck. set type to unknown
                createOrder.isReverseTrip=0
            }
        }
        else if(i==4)
        {
            if (checked) {
                createOrder.isDeliveryCharges=1
            }
            else {
                // if it is a uncheck. set type to unknown
                createOrder.isDeliveryCharges=0
            }
        }
    }


    fun priceCalculation()
    {

        var price: Price =getPrice()
       // var tfwsp=
        if(weight.toInt()>price.fws.toInt())
        {
            var nw=weight.toInt()-price.fws.toInt()
        }

    }

    private fun loadAddressData()
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "my_address_list"
        map["seller_type"] = "customer"//"agent"

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<Address>>> {
                        override val deferred: Deferred<BaseResponse<List<Address>>>
                            get() = ApiClient.authorizedApiService.addressList(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_ADDRESS_LIST
                        override val repoListener: RepoListener
                            get() = this@CreateShipmentViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                       // onAddressListReceived.value =  this.response.data.map { it.toRecyclerItem() }
                    onAddressListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    private fun Address.toRecyclerItem() = RecyclerItem(
        data = this,
        variableId = BR.item,
        layoutId = R.layout.row_address
    )

    class AddressItemViewModel(val address: Address) {
        fun onClick() {
            // handling logic
            if(!address.isSelect)
            address.isSelect=true

            Log.e("aress",""+address.isSelect)
        }
    }
}