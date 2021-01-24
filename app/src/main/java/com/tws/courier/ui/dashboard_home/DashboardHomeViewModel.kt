package com.tws.courier.ui.dashboard_home

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
import com.tws.courier.domain.models.DashBoard
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class DashboardHomeViewModel: BaseViewModel()
{
    val onGetDashDataReceived = MutableLiveData<SingleActionEvent<List<String>>>()
    val navigateToOrders = MutableLiveData<SingleActionEvent<Int>>()


    var newOrder: String = ""
    var pickup: String = ""
    var undelivered: String = ""
    var outForDelivery: String = ""
    var delivered: String = ""

    fun orders(param:Int) {
        navigateToOrders.value = SingleActionEvent(param)
    }

    fun requestDashdata() {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "user_get_dashdata"
        map["seller_type"] = "customer"//"agent"

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<String>>> {
                        override val deferred: Deferred<BaseResponse<List<String>>>
                            get() = ApiClient.authorizedApiService.getDashData(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_DASHDATA
                        override val repoListener: RepoListener
                            get() = this@DashboardHomeViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onGetDashDataReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }
}