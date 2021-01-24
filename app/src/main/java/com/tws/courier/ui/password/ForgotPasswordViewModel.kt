package com.tws.courier.ui.password

import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.ForgotPasswordResponse
import com.tws.courier.domain.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : BaseViewModel()
{
    val navigateToRegister = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToLogin = MutableLiveData<SingleActionEvent<Boolean>>()
    val onForgetSuccessful = MutableLiveData<SingleActionEvent<ForgotPasswordResponse>>()
    val onForgetMessage = MutableLiveData<SingleActionEvent<String>>()


    var email: String = ""

    fun goToLogin() {
        closeKeyBoard()
        navigateToLogin.value = SingleActionEvent(true)
    }

    fun goToRegister() {
        closeKeyBoard()
        navigateToRegister.value = SingleActionEvent(true)
    }

    fun forgetPassword()
    {
        val map = HashMap<String, String>()
        map["email"] = email.trim()
        map["method_name"] = "forgotpassword"
        map["seller_type"] = "customer"


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<ForgotPasswordResponse>> {
                        override val deferred: Deferred<BaseResponse<ForgotPasswordResponse>>
                            get() = ApiClient.apiService.forget(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.FORGOT_PASSWORD
                        override val repoListener: RepoListener
                            get() = this@ForgotPasswordViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                       // AppManager.setUser(this.response.data)
                        onForgetMessage.value = SingleActionEvent(this.response.message)
                        onForgetSuccessful.value = SingleActionEvent(this.response.data)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }




}