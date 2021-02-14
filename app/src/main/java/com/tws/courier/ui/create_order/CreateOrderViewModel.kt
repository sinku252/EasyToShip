package com.tws.courier.ui.create_order

import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.maps.android.SphericalUtil
import com.tws.courier.AppManager
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
import com.tws.courier.domain.utils.IConstant
import com.tws.courier.getObjectByType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreateOrderViewModel/*(private val pref: KotlinPreferencesHelper) */ : BaseViewModel()
{
    val navigateToPlace = MutableLiveData<SingleActionEvent<Boolean>>()
    val selectDateTime = MutableLiveData<SingleActionEvent<Boolean>>()
    val showBottomPopUp = MutableLiveData<SingleActionEvent<Boolean>>()
    val showViewByType = MutableLiveData<SingleActionEvent<Demo>>()
    val showViewByStep = MutableLiveData<SingleActionEvent<Int>>()
    val showInputDialog = MutableLiveData<SingleActionEvent<Int>>()
    val showContactList = MutableLiveData<SingleActionEvent<Int>>()

    val journeyType = MutableLiveData<SingleActionEvent<Int>>()
    val onOrderPriceSuccessful = MutableLiveData<SingleActionEvent<OrderPrice>>()
    val onStep1 = MutableLiveData<SingleActionEvent<String>>()
    val onOrderSuccessful = MutableLiveData<SingleActionEvent<OrderSuccess>>()
    val onCheckBoxSelect = MutableLiveData<SingleActionEvent<CheckBox>>()

    val navigateToInsurance = MutableLiveData<SingleActionEvent<Boolean>>()


    lateinit var allVehicleTypeList: ArrayList<OrderCalculation>



    var fromLocation: String = ""
    var toLocation: String = ""

    var step: Int =0

    var formatedDate = ""
    var selectedDate = ""
    var pickupMobileNumber:String = ""
    var deliveryMobileNumber = ""
    var weight = ""
    var porterValue = ""
    var dimension = ""
    var payment = ""
    var orderPriceCalculted = ""

    var radio_checked = MutableLiveData<Int>()

    init{
        pickupMobileNumber=AppManager.getUser()?.mobile ?: ""
    }

    init{
        radio_checked.postValue(1)//def value
    }

    var oneWayTwoWay = MutableLiveData<Int>()
    init{
        oneWayTwoWay.postValue(R.id.rb_round_trip)//def value
    }
    var domestic = MutableLiveData<Int>()
    init{
        domestic.postValue(1)//def value
    }
    var importExport = MutableLiveData<Int>()
    init{
        importExport.postValue(R.id.rb_export)//def value
    }
    var personalCommercial = MutableLiveData<Int>()
    init{
        personalCommercial.postValue(R.id.rb_personal)//def value
    }

    var vehicleType = MutableLiveData<Int>()
    init{
        vehicleType.postValue(1)//def value
    }

    var transmissionSpeed = MutableLiveData<Int>()
    init{
        transmissionSpeed.postValue(-1)//def value
    }
    var paymentMode = MutableLiveData<Int>()
    init{
        paymentMode.postValue(1)//def value
    }

    fun addFrom()
    {
        navigateToPlace.value = SingleActionEvent(true)
    }

    fun selectDate()
    {
        selectDateTime.value = SingleActionEvent(true)
    }

    fun addTo()
    {
        navigateToPlace.value = SingleActionEvent(false)
    }

    fun showInputDialog(type: Int)
    {
        showInputDialog.value = SingleActionEvent(type)
    }

    fun showContactist(type: Int)
    {
        showContactList.value = SingleActionEvent(type)
    }

    fun btnClickJourneyType(view: View, type: Int)
    {// var demo= Demo(view, type)
        journeyType.value = SingleActionEvent(type)
    }

    fun btnClick(view: View, type: Int) {
        var demo= Demo(view, type)
        showViewByType.value = SingleActionEvent(demo)
        /*if(type==0)
        {

        }
        else if(type==1)
        {
            showViewByType.value = SingleActionEvent(demo)
        }
        else if(type==2)
        {
            showViewByType.value = SingleActionEvent(demo)
        }*/

    }

    fun onTypeChecked(orderValidation: OrderValidation, checked: Boolean, i: Int) {
        var checkBox:CheckBox=CheckBox(i, checked)
        if(i==1)
        {
            if(orderValidation.journeyType==1)
            {
                if (checked) {
                    orderValidation.bikeValues.insurance="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.bikeValues.insurance="0"
                }
            }
            else if(orderValidation.journeyType==2)
            {
                if (checked) {
                    orderValidation.localTruckValues.insurance="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.localTruckValues.insurance="0"
                }
            }
            else if(orderValidation.journeyType==3)
            {
                if(orderValidation.domesticValues.domesticType.equals("domesticTruck"))
                {
                    if (checked) {
                        orderValidation.domesticValues.domesticTruckValues?.insurance="1"
                    }
                    else {
                        // if it is a uncheck. set type to unknown
                        orderValidation.domesticValues.domesticTruckValues?.insurance="0"
                    }
                }
                else if(orderValidation.domesticValues.domesticType.equals("domesticTrain"))
                {
                    if (checked) {
                        orderValidation.domesticValues.domesticTrainValues?.insurance="1"
                    }
                    else {
                        // if it is a uncheck. set type to unknown
                        orderValidation.domesticValues.domesticTrainValues?.insurance="0"
                    }
                }
                else if(orderValidation.domesticValues.domesticType.equals("domesticFlight"))
                {
                    if (checked) {
                        orderValidation.domesticValues.domesticFlightValues?.insurance="1"
                    }
                    else {
                        // if it is a uncheck. set type to unknown
                        orderValidation.domesticValues.domesticFlightValues?.insurance="0"
                    }
                }
            }
            else if(orderValidation.journeyType==4)
            {
                if (checked) {
                    orderValidation.internationalValues.insurance="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.internationalValues.insurance="0"
                }
            }

        }
        else if(i==2)
        {
            if(orderValidation.journeyType==2)
            {
                if (checked) {
                    orderValidation.localTruckValues.porter="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.localTruckValues.porter="0"
                }
            }
        }
        else if(i==3)
        {
            if(orderValidation.journeyType==1)
            {
                if (checked) {
                    orderValidation.bikeValues.dc="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.bikeValues.dc="0"
                }
            }
            else if(orderValidation.journeyType==2)
            {
                if (checked) {
                    orderValidation.localTruckValues.dc="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.localTruckValues.dc="0"
                }
            }
            else if(orderValidation.journeyType==3)
            {
                if(orderValidation.domesticValues.domesticType.equals("domesticTruck"))
                {
                    if (checked) {
                        orderValidation.domesticValues.domesticTruckValues?.dc="1"
                    }
                    else {
                        // if it is a uncheck. set type to unknown
                        orderValidation.domesticValues.domesticTruckValues?.dc="0"
                    }
                }
                else if(orderValidation.domesticValues.domesticType.equals("domesticTrain"))
                {
                    if (checked) {
                        orderValidation.domesticValues.domesticTrainValues?.dc="1"
                    }
                    else {
                        // if it is a uncheck. set type to unknown
                        orderValidation.domesticValues.domesticTrainValues?.dc="0"
                    }
                }
               /* else if(orderValidation.domesticValues.domesticType.equals("domesticFlight"))
                {
                    if (checked) {
                        orderValidation.domesticValues.domesticFlightValues?.dc="1"
                    }
                    else {
                        // if it is a uncheck. set type to unknown
                        orderValidation.domesticValues.domesticFlightValues?.dc="0"
                    }
                }*/
            }
           /* else if(orderValidation.journeyType==4)
            {
                if (checked) {
                    orderValidation.internationalValues.dc="1"
                }
                else {
                    // if it is a uncheck. set type to unknown
                    orderValidation.internationalValues.dc="0"
                }
            }*/
        }
        onCheckBoxSelect.value = SingleActionEvent(checkBox)
      //  else if()


    }

    fun checkStep2Validation(orderValidation: OrderValidation)
    {
        if(orderValidation.journeyType==1)//bike
        {
            if (TextUtils.isEmpty(selectedDate.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_date_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(pickupMobileNumber.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_pickup_number_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(deliveryMobileNumber.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_delivery_number_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(weight.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_weight_empty)
                    )
                )
                return
            }
            else if (orderValidation.bikeValues.shipmentType.equals("document"))  {
                if (weight.toInt()>10)  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_weight_max)
                        )
                    )
                    return
                }
            }
            /*else if (orderValidation.bikeValues.dc.equals("0"))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_dc_empty)
                    )
                )
                return
            }*/
            var orderCalculation:OrderCalculation?=getObjectByType("local",
                "bike",
                allVehicleTypeList)
            if (weight.toFloat()> orderCalculation!!.maxWeightLimit?.toFloat())  {
                var message=IConstant.maxWeight+"bike "+orderCalculation.maxWeightLimit+"kg"
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        message
                    )
                )
                return
            }
            if (weight.toFloat()<orderCalculation!!.minWeightLimit?.toFloat())  {
                var message=IConstant.miniWeight+"bike "+orderCalculation.minWeightLimit+"kg"
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        message
                    )
                )
                return
            }

        }
        else  if(orderValidation.journeyType==2)//local truck
        {
            if (TextUtils.isEmpty(selectedDate.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_date_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(pickupMobileNumber.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_pickup_number_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(deliveryMobileNumber.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_delivery_number_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(weight.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_weight_empty)
                    )
                )
                return
            }
            else if (orderValidation.localTruckValues.porter.equals("1"))  {
                if (TextUtils.isEmpty(porterValue))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_porter_empty)
                        )
                    )
                    return
                }
              /*  showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_select_porter)
                    )
                )
                return*/
            }
           /* else if (TextUtils.isEmpty(porterValue))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_porter_empty)
                    )
                )
                return
            }*/
            /*else if (TextUtils.isEmpty(porterValue.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_select_porter)
                    )
                )
                return
            }*/

            else if (TextUtils.isEmpty(orderValidation.localTruckValues.personalOrCommercial.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_personal_commercial_empty)
                    )
                )
                return
            }

           /* else if (orderValidation.localTruckValues.dc.equals("0"))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_dc_empty)
                    )
                )
                return
            }*/

            var orderCalculation:OrderCalculation?=getObjectByType("local",
                "truck",
                allVehicleTypeList)
             if (weight.toFloat()<orderCalculation!!.minWeightLimit?.toFloat()) {
                 var message=IConstant.miniWeight+"truck "+orderCalculation.minWeightLimit+"kg"
                 //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                 showInputError.value = SingleActionEvent(
                     BaseFragment.InputError(
                         InputErrorType.MESSAGE,
                         message
                     )
                 )
                 return
             }

            if (weight.toFloat()>orderCalculation!!.maxWeightLimit?.toFloat()) {
                var message=IConstant.maxWeight+"truck "+orderCalculation.maxWeightLimit+"kg"
                //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        message
                    )
                )
                return
            }
        }
        else if(orderValidation.journeyType==3)
        {
            if(orderValidation.domesticValues.domesticType.equals("domesticFlight"))
            {
                if (TextUtils.isEmpty(selectedDate.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_date_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(pickupMobileNumber.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_pickup_number_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(deliveryMobileNumber.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_delivery_number_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(weight.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_weight_empty)
                        )
                    )
                    return
                }
                else if (orderValidation.domesticValues.domesticFlightValues?.shipmentType.equals("parcel"))  {
                    if (TextUtils.isEmpty(dimension))  {
                        showInputError.value = SingleActionEvent(
                            BaseFragment.InputError(
                                InputErrorType.MESSAGE,
                                AppManager.getString(R.string.err_dimension_empty)
                            )
                        )
                        return
                    }
                }
                else if(TextUtils.isEmpty(orderValidation.domesticValues.domesticFlightValues?.personalOrCommercial))
                {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_personal_commercial_empty)
                        )
                    )
                    return
                }

                var orderCalculation:OrderCalculation?=getObjectByType("national",
                    "flight",
                    allVehicleTypeList)
                if (weight.toFloat()<orderCalculation!!.minWeightLimit?.toFloat()) {
                    var message=IConstant.miniWeight+"flight "+orderCalculation.minWeightLimit+" kg"
                    //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            message
                        )
                    )
                    return
                }
              /*  else if (orderValidation.domesticValues.domesticFlightValues?.insurance.equals("1"))  {
                    if(TextUtils.isEmpty(orderValidation.domesticValues.domesticFlightValues?.personalOrCommercial))
                    {
                        showInputError.value = SingleActionEvent(
                            BaseFragment.InputError(
                                InputErrorType.MESSAGE,
                                AppManager.getString(R.string.err_personal_commercial_empty)
                            )
                        )
                        return
                    }
                }*/

                else if (orderValidation.domesticValues.domesticFlightValues?.shipmentType.equals("document"))  {
                    if (weight.toInt()>10)  {
                        showInputError.value = SingleActionEvent(
                            BaseFragment.InputError(
                                InputErrorType.MESSAGE,
                                AppManager.getString(R.string.err_weight_max)
                            )
                        )
                        return
                    }
                }

            }
            else if(orderValidation.domesticValues.domesticType.equals("domesticTrain"))
            {
                if (TextUtils.isEmpty(selectedDate.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_date_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(pickupMobileNumber.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_pickup_number_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(deliveryMobileNumber.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_delivery_number_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(weight.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_weight_empty)
                        )
                    )
                    return
                }
                else if(TextUtils.isEmpty(orderValidation.domesticValues.domesticTrainValues?.personalOrCommercial))
                {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_personal_commercial_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(dimension))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_dimension_empty)
                        )
                    )
                    return
                }
                /*else if (orderValidation.domesticValues.domesticTrainValues?.dc.equals("0"))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_dc_empty)
                        )
                    )
                    return
                }*/
                var orderCalculation:OrderCalculation?=getObjectByType("national",
                    "train",
                    allVehicleTypeList)
                if (weight.toFloat()<orderCalculation!!.minWeightLimit?.toFloat()) {
                    var message=IConstant.miniWeight+"train "+orderCalculation.minWeightLimit+" kg"
                    //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            message
                        )
                    )
                    return
                }
                /*else if (orderValidation.domesticValues.domesticTrainValues?.dc.equals("1"))  {
                    if(TextUtils.isEmpty(orderValidation.domesticValues.domesticTrainValues?.personalOrCommercial))
                    {
                        showInputError.value = SingleActionEvent(
                            BaseFragment.InputError(
                                InputErrorType.MESSAGE,
                                AppManager.getString(R.string.err_personal_commercial_empty)
                            )
                        )
                        return
                    }
                }*/
            }
            else if(orderValidation.domesticValues.domesticType.equals("domesticTruck"))
            {
                if (TextUtils.isEmpty(selectedDate.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_date_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(pickupMobileNumber.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_pickup_number_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(deliveryMobileNumber.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_delivery_number_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(weight.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_weight_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(dimension.trim()))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_dimension_empty)
                        )
                    )
                    return
                }

                else if(TextUtils.isEmpty(orderValidation.domesticValues.domesticTruckValues?.personalOrCommercial))
                {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_personal_commercial_empty)
                        )
                    )
                    return
                }
                /*else if (orderValidation.domesticValues.domesticTruckValues?.dc.equals("0"))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_dc_empty)
                        )
                    )
                    return
                }*/

                var orderCalculation:OrderCalculation?=getObjectByType("national",
                    "truck",
                    allVehicleTypeList)
                 if (weight.toFloat()<orderCalculation!!.minWeightLimit?.toFloat()) {
                     var message=IConstant.miniWeight+"truck "+orderCalculation.minWeightLimit+"kg"
                     //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                     showInputError.value = SingleActionEvent(
                         BaseFragment.InputError(
                             InputErrorType.MESSAGE,
                             message
                         )
                     )
                     return
                 }
              /*  if (weight.toFloat()>orderCalculation!!.maxWeightLimit?.toFloat()) {
                    var message=IConstant.maxWeight+"truck "+orderCalculation.maxWeightLimit+"kg"
                    //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            message
                        )
                    )
                    return
                }*/
                /*else if (orderValidation.domesticValues.domesticTruckValues?.dc.equals("1"))  {
                    if(TextUtils.isEmpty(orderValidation.domesticValues.domesticTruckValues?.personalOrCommercial))
                    {
                        showInputError.value = SingleActionEvent(
                            BaseFragment.InputError(
                                InputErrorType.MESSAGE,
                                AppManager.getString(R.string.err_personal_commercial_empty)
                            )
                        )
                        return
                    }
                }*/
            }
        }
        else  if(orderValidation.journeyType==4)//international
        {
            /*
            var dimention:String="",//if Parcel
            var insurance:String="",//if check personalOrCommercial
            var personalOrCommercial:String=""*/
            if (TextUtils.isEmpty(selectedDate.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_date_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(pickupMobileNumber.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_pickup_number_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(deliveryMobileNumber.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_delivery_number_empty)
                    )
                )
                return
            }
            else if (TextUtils.isEmpty(weight.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_weight_empty)
                    )
                )
                return
            }
            else if (orderValidation.internationalValues?.shipmentType.equals("parcel"))  {
                if (TextUtils.isEmpty(dimension))  {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_dimension_empty)
                        )
                    )
                    return
                }
            }
           /* else if (TextUtils.isEmpty(dimension.trim()))  {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_dimension_empty)
                    )
                )
                return
            }*/
            else if(TextUtils.isEmpty(orderValidation.internationalValues.personalOrCommercial))
            {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_personal_commercial_empty)
                    )
                )
                return
            }
            /*else if (orderValidation.internationalValues.insurance.equals("1"))  {
                if(TextUtils.isEmpty(orderValidation.internationalValues.personalOrCommercial))
                {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_personal_commercial_empty)
                        )
                    )
                    return
                }
            }*/
        }

       /* val formatter = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        val date: Date = formatter.parse(selectedDate)
        val strDate = formatter.format(date)*/
        var distance=SphericalUtil.computeDistanceBetween(orderValidation.startLatLng,
            orderValidation.endLatLng)
        var distanceKm=(distance/1000).toFloat()

        val map = HashMap<String, String>()
        map["method_name"]="view_orderprice"
        map["user_id"]=  AppManager.getUser()?.id ?: ""
        map["seller_type"]="customer"
        map["start_latitude"]=orderValidation?.startLatLng?.latitude.toString()
        map["start_longitude"]=orderValidation?.startLatLng?.longitude.toString()
        map["end_latitude"]=orderValidation?.endLatLng?.latitude.toString()
        map["end_longitude"]=orderValidation?.endLatLng?.longitude.toString()
        map["area_type"]=getAreaType(orderValidation.journeyType)
        map["shipment_type"]=getShipmentType(orderValidation)
        map["vehicle_type"]=getVehicleType(orderValidation?.journeyType,orderValidation.domesticValues.domesticType)
        map["transport_type"]=getTranspoetType(orderValidation)
        map["weight"]=weight
        map["distance"]=distanceKm.toString()
        map["pickup_mobile_number"]=pickupMobileNumber
        map["delivery_mobile_number"]=deliveryMobileNumber
        map["schedule"]=formatedDate
        map["is_insurance"]=getCheckBoxValue(orderValidation,"insurance")
        map["is_porter"]=getCheckBoxValue(orderValidation,"porter")
        map["porter_count"]=porterValue
        map["is_delivery_charges"]=getCheckBoxValue(orderValidation,"dc")
        map["order_type"]=getOrderType(orderValidation)
        map["transmission_speed"]=orderValidation.transmissionSpeed
        if (!TextUtils.isEmpty(dimension.trim()))
        {
            var lstValues: List<String> = dimension.split("x").map { it -> it.trim() }
            map["height"]=lstValues[0]
            map["length"]=lstValues[1]
            map["width"]=lstValues[2]
        }
        else
        {
            map["height"]=""
            map["length"]=""
            map["width"]=""
        }

        Log.e("fafffffffff",map.toString())

        getOrderPrice(map)


    }

    fun checkValidation(orderValidation: OrderValidation)
    {
        closeKeyBoard()
        if (orderValidation.journeyType==0) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_vehicle_empty)
                )
            )
            return
        }
        if(orderValidation.journeyType==1)//bike
        {
            if (TextUtils.isEmpty(fromLocation.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_from_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(toLocation.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_to_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(orderValidation.bikeValues.shipmentType.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_shipment_type_empty)
                    )
                )
                return
            }
            allVehicleTypeList.forEach { println(it) }
            var distance=SphericalUtil.computeDistanceBetween(orderValidation.startLatLng,
                orderValidation.endLatLng)
            var distanceKm=(distance/1000).toFloat()
            var orderCalculation:OrderCalculation?=getObjectByType("local",
                "bike",
                allVehicleTypeList)
            println("distanceKm" + distanceKm)
            if (distanceKm>orderCalculation!!.maxDistance?.toFloat()) {
                var message=IConstant.maxDistance+"bike "+orderCalculation.maxDistance+"km"
                //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        message
                    )
                )
                return
            }


           // showViewByStep.value= SingleActionEvent(1)
        }
        else if(orderValidation.journeyType==2)//local truck
        {
            if (TextUtils.isEmpty(fromLocation.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_from_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(toLocation.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_to_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(orderValidation.localTruckValues.shipmentType.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_shipment_type_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(orderValidation.localTruckValues.oneWayTwoWay.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_one_empty)
                    )
                )
                return
            }
            var distance=SphericalUtil.computeDistanceBetween(orderValidation.startLatLng,
                orderValidation.endLatLng)
            var distanceKm=(distance/1000).toFloat()
            println("distanceKm" + distanceKm)
            var orderCalculation:OrderCalculation?=getObjectByType("local",
                "truck",
                allVehicleTypeList)
            if (distanceKm>orderCalculation!!.maxDistance?.toFloat()) {
                var message=IConstant.maxDistance+"truck "+orderCalculation.maxDistance+"km"
                //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        message
                    )
                )
                return
            }
        }
        else if(orderValidation.journeyType==3)//domestic
        {



            if (TextUtils.isEmpty(orderValidation.domesticValues.domesticType.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_empty_domestic)
                    )
                )
                return
            }
            if (orderValidation.domesticValues.domesticType.equals("domesticTruck"))
            {
                var distance=SphericalUtil.computeDistanceBetween(orderValidation.startLatLng,
                    orderValidation.endLatLng)
                var distanceKm=(distance/1000).toFloat()
                println("distanceKm" + distanceKm)
                var orderCalculation:OrderCalculation?=getObjectByType("national","truck",allVehicleTypeList)

                if (TextUtils.isEmpty(fromLocation.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_from_empty)
                        )
                    )
                    return
                }
                else if (TextUtils.isEmpty(toLocation.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_to_empty)
                        )
                    )
                    return
                }

                else if (TextUtils.isEmpty(orderValidation.domesticValues.domesticTruckValues?.shipmentType?.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_shipment_type_empty)
                        )
                    )
                    return
                }
                else if (orderCalculation!!.minDistance?.toFloat()>distanceKm) {
                    var message=IConstant.miniDistance+"truck "+orderCalculation.minDistance+"km"
                    //var message =AppManager.getString(R.string.err_max_km,"bike","bike")
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            message
                        )
                    )
                    return
                }

                Log.e("gsdsdgsg",orderValidation.domesticValues.domesticTruckValues?.shipmentType)
            }
            else if(orderValidation.domesticValues.domesticType.equals("domesticTrain"))
            {
                if (TextUtils.isEmpty(fromLocation.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_from_empty)
                        )
                    )
                    return
                }
                if (TextUtils.isEmpty(toLocation.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_to_empty)
                        )
                    )
                    return
                }
                if (TextUtils.isEmpty(orderValidation.domesticValues.domesticTrainValues?.shipmentType?.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_shipment_type_empty)
                        )
                    )
                    return
                }


            }
            else if(orderValidation.domesticValues.domesticType.equals("domesticFlight"))
            {
                if (TextUtils.isEmpty(fromLocation.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_from_empty)
                        )
                    )
                    return
                }
                if (TextUtils.isEmpty(toLocation.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_to_empty)
                        )
                    )
                    return
                }
                if (TextUtils.isEmpty(orderValidation.domesticValues.domesticFlightValues?.shipmentType?.trim())) {
                    showInputError.value = SingleActionEvent(
                        BaseFragment.InputError(
                            InputErrorType.MESSAGE,
                            AppManager.getString(R.string.err_shipment_type_empty)
                        )
                    )
                    return
                }
            }


            Log.e("fafasfsaf",""+orderValidation)


        }

        else if(orderValidation.journeyType==4)//international
        {
            if (TextUtils.isEmpty(orderValidation.internationalValues.importExport.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_empty_import_export)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(fromLocation.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_from_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(toLocation.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_to_empty)
                    )
                )
                return
            }
            if (TextUtils.isEmpty(orderValidation.internationalValues.shipmentType.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.MESSAGE,
                        AppManager.getString(R.string.err_shipment_type_empty)
                    )
                )
                return
            }
        }


        /*"method_name":"step1_country_validation",
        "shipment_type":"parcel",
        "end_latitude":"55.3781",
        "end_longitude":"3.4360",
        "start_latitude":"27.9174",
        "start_longitude":"90.1384",
        "vehicle_type":"truck",
        "area_type":"national",
        "transport_type":"export"*/

        val map = HashMap<String, String>()
        map["method_name"]="step1_country_validation"
        map["start_latitude"]=orderValidation?.startLatLng?.latitude.toString()
        map["start_longitude"]=orderValidation?.startLatLng?.longitude.toString()
        map["end_latitude"]=orderValidation?.endLatLng?.latitude.toString()
        map["end_longitude"]=orderValidation?.endLatLng?.longitude.toString()
        map["area_type"]=getAreaType(orderValidation.journeyType)
        map["shipment_type"]=getShipmentType(orderValidation)
        map["vehicle_type"]=getVehicleType(orderValidation?.journeyType,orderValidation.domesticValues.domesticType)
        map["transport_type"]=getTranspoetType(orderValidation)
        checkStepOne(map)



    }

    fun confimOrder(orderValidation: OrderValidation,orderPrice: OrderPrice)
    {
        var distance=SphericalUtil.computeDistanceBetween(orderValidation.startLatLng,
            orderValidation.endLatLng)
        var distanceKm=(distance/1000).toFloat()

        if (TextUtils.isEmpty(payment.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_payment_mode_empty)
                )
            )
            return
        }
        val map = HashMap<String, String>()
        map["method_name"]="add_orderprices"
        map["user_id"]=  AppManager.getUser()?.id ?: ""
        map["seller_type"]="customer"
        map["start_latitude"]=orderValidation?.startLatLng?.latitude.toString()
        map["start_longitude"]=orderValidation?.startLatLng?.longitude.toString()
        map["end_latitude"]=orderValidation?.endLatLng?.latitude.toString()
        map["end_longitude"]=orderValidation?.endLatLng?.longitude.toString()
        map["area_type"]=getAreaType(orderValidation.journeyType)
        map["shipment_type"]=getShipmentType(orderValidation)
        map["vehicle_type"]=getVehicleType(orderValidation?.journeyType,orderValidation.domesticValues.domesticType)
        map["transport_type"]=getTranspoetType(orderValidation)
        map["weight"]=weight
        map["distance"]=distanceKm.toString()
        map["pickup_mobile_number"]=pickupMobileNumber
        map["delivery_mobile_number"]=deliveryMobileNumber
        map["schedule"]=formatedDate
        map["is_insurance"]=getCheckBoxValue(orderValidation,"insurance")
        map["is_porter"]=getCheckBoxValue(orderValidation,"porter")
        map["porter_count"]=porterValue
        map["is_delivery_charges"]=getCheckBoxValue(orderValidation,"dc")
        map["order_type"]=getOrderType(orderValidation)

        map["transmission_speed"]=orderValidation.transmissionSpeed
        map["total_distance_price"]= orderPrice.totalDistancePrice
        map["total_wieght_price"]= orderPrice.totalWieghtPrice
        map["total_fair"]=orderPrice.totalFair
        map["total_insurance_charge"]=orderPrice.totalInsuranceCharge
        map["total_porter_charge"]= orderPrice.totalPorterCharge
        map["total_delivery_challan"]=orderPrice.totalDeliveryChallan
        map["total_gst_charge"]=orderPrice.totalGstCharge
        map["total_peak_charge"]=orderPrice.totalPeakCharge
        map["total_other_charge"]= orderPrice.totalOtherCharge
        map["total_amount_price"]= orderPrice.totalAmountPrice
        map["payment_mode"]=orderValidation.paymentMode
        map["payment_status"]="1"
        /*map["payment_mode"]=payment
        map["payment_status"]="1"*/
        map["destination"]=orderValidation.destination
        map["origin"]=orderValidation.origin

        if (!TextUtils.isEmpty(dimension.trim()))
        {
            var lstValues: List<String> = dimension.split("x").map { it -> it.trim() }
            map["height"]=lstValues[0]
            map["length"]=lstValues[1]
            map["width"]=lstValues[2]
        }
        else
        {
            map["height"]=""
            map["length"]=""
            map["width"]=""
        }

        Log.e("Faaaa",map.toString())

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
                            get() = this@CreateOrderViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        onOrderSuccessful.value = SingleActionEvent(this.response.data)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }


    }




    fun getShipmentType(orderValidation: OrderValidation):String
    {
        var type=""
        if (orderValidation.journeyType == 1) {
            type=orderValidation.bikeValues.shipmentType
        } else if (orderValidation.journeyType === 2) {
            type=orderValidation.localTruckValues.shipmentType
        } else if (orderValidation.journeyType === 3) {
            if (orderValidation.domesticValues.domesticType.equals("domesticTruck",
                    ignoreCase = true)) {
                type = orderValidation.domesticValues.domesticTruckValues?.shipmentType.toString()
            } else if (orderValidation.domesticValues.domesticType.equals("domesticTrain",
                    ignoreCase = true)) {
                type = orderValidation.domesticValues.domesticTrainValues?.shipmentType.toString()
            } else if (orderValidation.domesticValues.domesticType.equals("domesticFlight",
                    ignoreCase = true)) {
                type = orderValidation.domesticValues.domesticFlightValues?.shipmentType.toString()
            }
        } else if (orderValidation.journeyType === 4) {
            type=orderValidation.internationalValues.shipmentType
        }
        return type
    }
    fun getAreaType(journeyType: Int): String {
        var type = ""
        if (journeyType == 1 || journeyType == 2) {
            type = "local"
        } else if (journeyType == 3) {
            type = "national"
        } else if (journeyType == 4) {
            type = "international"
        }
        return type
    }
    fun getVehicleType(journeyType: Int, domestic: String): String {
        var type = ""
        if (journeyType == 1) {
            type = "bike"
        } else if (journeyType == 2) {
            type = "truck"
        } else if (journeyType == 3) {
            if (domestic.equals("domesticTruck", ignoreCase = true)) {
                type = "truck"
            } else if (domestic.equals("domesticTrain", ignoreCase = true)) {
                type = "train"
            } else if (domestic.equals("domesticFlight", ignoreCase = true)) {
                type = "flight"
            }
        } else if (journeyType == 4) {
            type = "flight"
        }
        return type
    }
    fun getTranspoetType(orderValidation: OrderValidation):String
    {
        var transpoetType = ""

        if (orderValidation.journeyType == 2) {
            transpoetType = orderValidation.localTruckValues.oneWayTwoWay
        } else if (orderValidation.journeyType == 4) {
            transpoetType = orderValidation.internationalValues.importExport
        }
        return transpoetType

    }
    fun getOrderType(orderValidation: OrderValidation):String
    {
        var orderType= ""
        if (orderValidation.journeyType == 2) {
            orderType = orderValidation.localTruckValues.oneWayTwoWay
        }
        else if (orderValidation.journeyType == 3) {
            if (orderValidation.domesticValues.domesticType.equals("domesticTruck",
                    ignoreCase = true)) {
                orderType = orderValidation.domesticValues.domesticTruckValues?.personalOrCommercial.toString()
            } else if (orderValidation.domesticValues.domesticType.equals("domesticTrain",
                    ignoreCase = true)) {
                orderType = orderValidation.domesticValues.domesticTrainValues?.personalOrCommercial.toString()
            } else if (orderValidation.domesticValues.domesticType.equals("domesticFlight",
                    ignoreCase = true)) {
                orderType = orderValidation.domesticValues.domesticFlightValues?.personalOrCommercial.toString()
            }
        }
        else  if (orderValidation.journeyType == 4) {
            orderType = orderValidation.internationalValues.personalOrCommercial
        }
        return orderType
    }
    fun getCheckBoxValue(orderValidation: OrderValidation,type: String):String
    {
        var checkValue=""

        if (orderValidation.journeyType == 1) {
            if(type.equals("insurance"))
            {
                checkValue=orderValidation.bikeValues.insurance
            }
            else if(type.equals("dc"))
            {
                checkValue=orderValidation.bikeValues.dc
            }
        }
        else if (orderValidation.journeyType == 2) {
            if(type.equals("insurance"))
            {
                checkValue=orderValidation.localTruckValues.insurance
            }
            else if(type.equals("porter"))
            {
                checkValue=orderValidation.localTruckValues.porterValue
            }
            else if(type.equals("dc"))
            {
                checkValue=orderValidation.localTruckValues.dc
            }
        } else if (orderValidation.journeyType == 3) {

            if (orderValidation.domesticValues.domesticType.equals("domesticTruck",ignoreCase = true))
            {
                if (type.equals("insurance")) {
                    checkValue = orderValidation.domesticValues.domesticTruckValues?.insurance.toString()
                } else if (type.equals("dc")) {
                    checkValue = orderValidation.domesticValues.domesticTruckValues?.dc.toString()
                }
            }
            else if (orderValidation.domesticValues.domesticType.equals("domesticTrain",ignoreCase = true))
            {
                if (type.equals("insurance")) {
                    checkValue = orderValidation.domesticValues.domesticTrainValues?.insurance.toString()
                } else if (type.equals("dc")) {
                    checkValue = orderValidation.domesticValues.domesticTrainValues?.dc.toString()
                }
            }


        }
        else if (orderValidation.journeyType == 4) {
            if(type.equals("insurance"))
            {
                checkValue=orderValidation.internationalValues.insurance
            }
        }
        return checkValue
    }


    fun getOrderPrice(params: HashMap<String, String>) {
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<OrderPrice>> {
                        override val deferred: Deferred<BaseResponse<OrderPrice>>
                            get() = ApiClient.apiService.viewOrderPrice(
                                params
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.VIEW_ORDER_PRICE
                        override val repoListener: RepoListener
                            get() = this@CreateOrderViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        onOrderPriceSuccessful.value = SingleActionEvent(this.response.data)
                        showViewByStep.value= SingleActionEvent(2)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


    fun checkStepOne(params: HashMap<String, String>) {
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<String>> {
                        override val deferred: Deferred<BaseResponse<String>>
                            get() = ApiClient.apiService.step1CountryValidation(
                                params
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.STEP1_COUNTRY_VALIDATION
                        override val repoListener: RepoListener
                            get() = this@CreateOrderViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        //onStep1.value = SingleActionEvent(this.response.data)
                        showViewByStep.value= SingleActionEvent(1)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


    fun navigateToInsurance() {
        navigateToInsurance.value = SingleActionEvent(true)
    }
}