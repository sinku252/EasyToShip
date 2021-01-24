package com.tws.courier.ui.orders

import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.Order
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class OrdersViewModel :BaseViewModel()
{
    val onOrdersReceived = MutableLiveData<SingleActionEvent<List<Order>>>()
    fun orders(param:Int) {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "get_order_list"
        map["delivery_status"] = param.toString()
        map["seller_type"] = "customer"//"agent"
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<Order>>> {
                        override val deferred: Deferred<BaseResponse<List<Order>>>
                            get() = ApiClient.authorizedApiService.getOrders(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_ORDERS
                        override val repoListener: RepoListener
                            get() = this@OrdersViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onOrdersReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }
}