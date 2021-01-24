package com.tws.courier.ui.address

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
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
import com.tws.courier.domain.models.Address
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class AddressViewModel : BaseViewModel()
{
    val onAddressSuccessful = MutableLiveData<SingleActionEvent<String>>()
    var radio_checked = MutableLiveData<Int>()

    var name: String = ""
    var mobile: String = ""
    var address1: String = ""
    var address2: String = ""
    var pincode: String = ""
   // var phone: String = ""
    var city: String = ""
    var state: String = ""
    var addressType: String = ""


    init{
        radio_checked.postValue(1)//def value
    }

    fun AddNewAddress() {

        closeKeyBoard()
        if (TextUtils.isEmpty(name.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.NAME,
                    AppManager.getString(R.string.err_name_empty)
                )
            )
            return
        }

        if (TextUtils.isEmpty(mobile.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PHONE,
                    AppManager.getString(R.string.err_mobile_empty)
                )
            )
            return
        }

        if (TextUtils.isEmpty(address1.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_address_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(address2.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_address_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(pincode.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PHONE,
                    AppManager.getString(R.string.err_pincode_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(city.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.NAME,
                    AppManager.getString(R.string.err_city_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(state.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.NAME,
                    AppManager.getString(R.string.err_state_empty)
                )
            )
            return
        }
    
        val map = HashMap<String, String>()
        /*map["email"] = email.trim()
        map["password"] = password.trim()
        map["method_name"] = "user_login"
        map["seller_type"] = "agent"*/

        map["method_name"]="add_new_address"
        map["user_id"]=AppManager.getUser()?.id ?: ""
        map["seller_type"]="customer"
        map["name"]=name.trim()
        map["house_no"]=address1.trim()
        map["address"]=address2.trim()
        map["city"]=city.trim()
        map["state"]=state.trim()
        map["country"]="india"
        map["pincode"]=pincode.trim()
        map["phone_no"]=mobile.trim()
        map["address_type"]=addressType.trim()




        /*  val map = JsonObject()
          map.addProperty("email",email.trim())
          map.addProperty("password",password.trim())
          map.addProperty("method_name", "user_login")
          map.addProperty("seller_type","agent")*/

        requestAddress(map)
    }
    fun requestAddress(params: HashMap<String, String>) {
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Address>> {
                        override val deferred: Deferred<BaseResponse<Address>>
                            get() = ApiClient.apiService.AddNewAddress(
                                params
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.ADD_ADDRESS
                        override val repoListener: RepoListener
                            get() = this@AddressViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        onAddressSuccessful.value = SingleActionEvent(this.response.message)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }
}