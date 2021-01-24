package com.tws.courier.ui.address.addressList

import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.Address
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class AddressListViewModel : BaseViewModel()
{
    val navigateToAddress = MutableLiveData<SingleActionEvent<Boolean>>()
    val onAddressListReceived = MutableLiveData<SingleActionEvent<List<Address>>>()
    val onAddressDelete = MutableLiveData<SingleActionEvent<String>>()
    /*init {
        requestAddress(boolean: Boolean)
    }*/



    fun requestAddress(boolean: Boolean) {

        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "my_address_list"
        map["seller_type"] = "customer"//"agent"
        if(boolean)
            map["address_type"] = "pickup"
        else
            map["address_type"] = "delivery"


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<Address>>> {
                        override val deferred: Deferred<BaseResponse<List<Address>>>
                            get() = ApiClient.authorizedApiService.addressList(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_ADDRESS_LIST
                        override val repoListener: RepoListener
                            get() = this@AddressListViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onAddressListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun deleteAddress(address: Address)
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "delete_myaddress"
        map["seller_type"] = "customer"//"agent"
        map["address_id"] = address.id

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.authorizedApiService.deleteAddress(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.DELETE_ADDRESS
                        override val repoListener: RepoListener
                            get() = this@AddressListViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onAddressDelete.value = SingleActionEvent(this.response.message)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun addAddress() {
        closeKeyBoard()
        navigateToAddress.value = SingleActionEvent(true)
    }
}