package com.tws.courier.ui.change_password

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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class ChangePasswordViewModel : BaseViewModel()
{
    val onChangePasswordSuccessful = MutableLiveData<SingleActionEvent<Boolean>>()

    var password: String = ""
    var confirmPassword: String = ""

    fun changePassword(user_id:String)
    {

        if (TextUtils.isEmpty(password.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PASSWORD,
                    AppManager.getString(R.string.err_password_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(confirmPassword.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.CONFIRM_PASSWORD,
                    AppManager.getString(R.string.err_confirm_password_empty)
                )
            )
            return
        }
        if (password != confirmPassword) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PASSWORD,
                    AppManager.getString(R.string.err_mismatch_password)
                )
            )
            return
        }

        val map = HashMap<String, String>()
        map["user_id"] = user_id.trim()
        map["password"] = password
        map["method_name"] = "update_password"
        map["seller_type"] = "customer"


        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.apiService.updatePassword(
                                map
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.UPDATE_PASSWORD
                        override val repoListener: RepoListener
                            get() = this@ChangePasswordViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                     //   AppManager.setUser(this.response.data)
                        onChangePasswordSuccessful.value = SingleActionEvent(true)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

}