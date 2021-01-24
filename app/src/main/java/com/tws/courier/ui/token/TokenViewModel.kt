package com.tws.courier.ui.token

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
import com.tws.courier.domain.utils.Utils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class TokenViewModel :BaseViewModel()
{
    val onTicketAddedSuccessful = MutableLiveData<SingleActionEvent<String>>()

    var subject: String = ""
    var message: String = ""


 fun submitTicket()
 {

     closeKeyBoard()

     if (TextUtils.isEmpty(subject.trim())) {
         showInputError.value = SingleActionEvent(
             BaseFragment.InputError(
                 InputErrorType.MESSAGE,
                 AppManager.getString(R.string.err_subject_empty)
             )
         )
         return
     }
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
     map["subject"] = subject.trim()
     map["message"] = message.trim()
     map["method_name"] = "add_ticket"
     map["seller_type"] = "customer"
     map["parent_id"] = ""
     map["status"] = "0"
     map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()

     uiScope.launch {
         (
                 object : RemoteRepo<BaseResponse<Any>> {
                     override val deferred: Deferred<BaseResponse<Any>>
                         get() = ApiClient.apiService.addTicket(
                             map
                         )
                     override val dataRequestType: Int
                         get() = DataRequestType.ADD_TICKET
                     override val repoListener: RepoListener
                         get() = this@TokenViewModel.repoListener
                 }
                 )
             .executeApiRequest()?.apply {
                 if (this.response.status.equals("1", ignoreCase = true)) {
                     onTicketAddedSuccessful.value = SingleActionEvent(this.response.message)
                 } else showToast.value = SingleActionEvent(this.response.message)
             }
     }
 }


}