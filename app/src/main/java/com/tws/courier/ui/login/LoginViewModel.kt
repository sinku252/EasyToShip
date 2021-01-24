package com.tws.courier.ui.login

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
import com.tws.courier.domain.models.User
import com.tws.courier.domain.utils.Utils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch


class LoginViewModel : BaseViewModel() {

    val onLoginSuccessful = MutableLiveData<SingleActionEvent<String>>()
    val navigateToForgotPassword = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToRegister = MutableLiveData<SingleActionEvent<Boolean>>()
    val navigateToHome = MutableLiveData<SingleActionEvent<Boolean>>()

    var email: String = ""
    var password: String = ""

    fun login() {
        closeKeyBoard()

        if (TextUtils.isEmpty(email.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.EMAIL,
                    AppManager.getString(R.string.err_email_empty)
                )
            )
            return
        } else if (Utils.isNumber(email.trim())) {
            if (!Utils.isMobileNumber(email.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.PHONE,
                        AppManager.getString(R.string.err_mobile_min_chars)
                    )
                )
                return
            }
        } else {
            if (!Utils.isValidEmail(email.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.EMAIL,
                        AppManager.getString(R.string.err_email_invalid)
                    )
                )
                return
            }
        }

        if (TextUtils.isEmpty(password.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PASSWORD,
                    AppManager.getString(R.string.err_password_empty)
                )
            )
            return
        }

        /*if (TextUtils.isEmpty(email.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.EMAIL,
                    AppManager.getString(R.string.err_email_empty)
                )
            )
            return
        }
        else if(Utils.isNumber(email.trim()))
        {
            if(!Utils.isValidPhoneNumber(email.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.PHONE,
                        AppManager.getString(R.string.err_mobile_min_chars)
                    )
                )
                return
            }
        }
        else
        {
            if (!Utils.isValidEmail(email.trim())) {
                showInputError.value = SingleActionEvent(
                    BaseFragment.InputError(
                        InputErrorType.EMAIL,
                        AppManager.getString(R.string.err_email_invalid)
                    )
                )
                return
            }
        }

        if (TextUtils.isEmpty(password.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PASSWORD,
                    AppManager.getString(R.string.err_password_empty)
                )
            )
            return
        }*/



        /*if (password.trim()?.length!! <= 5) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PASSWORD,
                    AppManager.getString(R.string.err_password_min_length)
                )
            )
            return
        }*/



        val map = HashMap<String, String>()
        map["email"] = email.trim()
        map["password"] = password.trim()
        map["method_name"] = "user_login"
        map["seller_type"] = "customer"

      /*  val map = JsonObject()
        map.addProperty("email",email.trim())
        map.addProperty("password",password.trim())
        map.addProperty("method_name", "user_login")
        map.addProperty("seller_type","agent")*/

        requestUserLogin(map)
    }

    fun navigateToForgotPassword() {
        navigateToForgotPassword.value = SingleActionEvent(true)
    }

    fun requestUserLogin(params: HashMap<String, String>) {
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<User>> {
                        override val deferred: Deferred<BaseResponse<User>>
                            get() = ApiClient.apiService.loginUser(
                                params
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.USER_LOGIN
                        override val repoListener: RepoListener
                            get() = this@LoginViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        AppManager.setUser(this.response.data)
                        onLoginSuccessful.value = SingleActionEvent(this.response.message)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun goToRegister() {
        closeKeyBoard()
        navigateToRegister.value = SingleActionEvent(true)
    }

    fun goToHome() {
        closeKeyBoard()
        navigateToHome.value = SingleActionEvent(true)
    }
}