package com.tws.courier.ui.help

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
import com.tws.courier.domain.models.Help
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class HelpViewModel : BaseViewModel()
{
    val navigateToChat = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToToken = MutableLiveData<SingleActionEvent<Boolean>>()

    val onTicketListReceived = MutableLiveData<SingleActionEvent<List<Help>>>()


    fun addNewToken()
    {
        closeKeyBoard()
        navigateToToken.value = SingleActionEvent(true)
    }

    init {
        getTicketList()
    }

    fun getTicketList()
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "ticket_list"
        map["seller_type"] = "customer"//"agent"
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<Help>>> {
                        override val deferred: Deferred<BaseResponse<List<Help>>>
                            get() = ApiClient.authorizedApiService.ticketList(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_TICKET_LIST
                        override val repoListener: RepoListener
                            get() = this@HelpViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onTicketListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


    fun goToChat()
    {
        closeKeyBoard()
        navigateToChat.value = SingleActionEvent(true)
    }
    fun goToToken()
    {
        closeKeyBoard()
        navigateToToken.value = SingleActionEvent(true)
    }

}