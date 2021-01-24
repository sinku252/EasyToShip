package com.tws.courier.ui.home

import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.OrderCalculation
import com.tws.courier.domain.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch


class HomeViewModel : BaseViewModel()
{
    val onListReceived = MutableLiveData<SingleActionEvent<List<OrderCalculation>>>()

    init {
        getCalculationList()
    }

    private fun getCalculationList() {

        val map = HashMap<String, String>()
        map["method_name"] = "get_order_calculation_details"


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<OrderCalculation>>> {
                        override val deferred: Deferred<BaseResponse<List<OrderCalculation>>>
                            get() = ApiClient.authorizedApiService.getOrderCalculationDetails(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_ORDER_CALCULATION_DETAILS
                        override val repoListener: RepoListener
                            get() = this@HomeViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                    // onAddressListReceived.value =  this.response.data.map { it.toRecyclerItem() }
                        //pref.allSubject = Gson().toJson(this.response.data)
                        onListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun sendFCM(string: String)
    {
        val map = HashMap<String, String>()
        map["method_name"] = "upDateFCMToken"
        map["user_id"] = "upDateFCMToken"
        map["token"] = string


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.apiService.upDateFCMToken(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.UPDATE_FCM_TOKEN
                        override val repoListener: RepoListener
                            get() = this@HomeViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                      /*  AppManager.setUser(this.response.data)
                        onLoginSuccessful.value = SingleActionEvent(this.response.message)*/
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


}