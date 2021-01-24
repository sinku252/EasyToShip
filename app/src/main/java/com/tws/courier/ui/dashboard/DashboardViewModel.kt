package com.tws.courier.ui.dashboard

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
import com.tws.courier.domain.models.Bank
import com.tws.courier.domain.models.HomeSlider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class DashboardViewModel : BaseViewModel()
{
    val navigateToDashboard = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToAccountSetting = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToHelp = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToCreateShipment = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToWallet = MutableLiveData<SingleActionEvent<Boolean>>()

    val onsliderListReceived = MutableLiveData<SingleActionEvent<List<HomeSlider>>>()

    fun navigateToDashboard() {
        navigateToDashboard.value = SingleActionEvent(true)
    }
    fun navigateToWallet() {
        navigateToWallet.value = SingleActionEvent(true)
    }

    fun navigateToAccountSetting() {
        navigateToAccountSetting.value = SingleActionEvent(true)
    }

    fun navigateToHelp() {
        navigateToHelp.value = SingleActionEvent(true)
    }

    fun navigateToCreateShipment() {
        navigateToCreateShipment.value = SingleActionEvent(true)
    }

    fun getSlider()
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "home_page_sliders"
        map["seller_type"] = "customer"//"agent"

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<HomeSlider>>> {
                        override val deferred: Deferred<BaseResponse<List<HomeSlider>>>
                            get() = ApiClient.authorizedApiService.getSlider(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_HOME_SLIDER
                        override val repoListener: RepoListener
                            get() = this@DashboardViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onsliderListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }



}