package com.tws.courier.ui.chats

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
import com.tws.courier.domain.models.Chat
import com.tws.courier.domain.models.Help
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class ChatViewModel :BaseViewModel()
{
    val onMsgSend = MutableLiveData<SingleActionEvent<Chat>>()
    val onChatListReceived = MutableLiveData<SingleActionEvent<List<Chat>>>()
    var message: String = ""

    init {
        getChatList()
    }


    fun getChatList()
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "chats_list"
        map["seller_type"] = "customer"//"agent"


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<Chat>>> {
                        override val deferred: Deferred<BaseResponse<List<Chat>>>
                            get() = ApiClient.authorizedApiService.chatList(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_CHAT_LIST
                        override val repoListener: RepoListener
                            get() = this@ChatViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onChatListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun sendMsg()
    {

        closeKeyBoard()

        if (TextUtils.isEmpty(message.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_message_empty)
                )
            )
            return
        }

        val map = HashMap<String, String>()
        map["user_id"] = AppManager.getUser()?.id ?: ""
        map["message"] = message.trim()
        map["method_name"] = "chats_add"
        map["seller_type"] = "customer"


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Chat>> {
                        override val deferred: Deferred<BaseResponse<Chat>>
                            get() = ApiClient.apiService.sendMsg(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.SEND_MSG
                        override val repoListener: RepoListener
                            get() = this@ChatViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        onMsgSend.value = SingleActionEvent(this.response.data)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }
}