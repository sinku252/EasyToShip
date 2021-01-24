package com.tws.courier.ui.signup

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

class SignUpViewModel  : BaseViewModel()
{
    val navigateToLogin = MutableLiveData<SingleActionEvent<Boolean>>()
    val onRegisterSuccessful = MutableLiveData<SingleActionEvent<String>>()

    var userName: String = ""
    var company: String = ""
    var panNumber: String = ""
    var gst: String = ""
    var email: String = ""
    var mobileNumber: String = ""
    var password: String = ""


    fun register()
    {

        closeKeyBoard()
        if (TextUtils.isEmpty(userName.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_username_empty)
                )
            )
            return
        }
        if (TextUtils.isEmpty(company.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_company_empty)
                )
            )
            return
        }

        if (TextUtils.isEmpty(panNumber.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_pannumber_empty)
                )
            )
            return
        }

        if (panNumber.trim()?.length!! <= 9) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_pannumber_min_length)
                )
            )
            return
        }

        if (TextUtils.isEmpty(gst.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_gst_empty)
                )
            )
            return
        }
        if (gst.trim()?.length!! <= 14) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.MESSAGE,
                    AppManager.getString(R.string.err_gst_min_length)
                )
            )
            return
        }

        if (TextUtils.isEmpty(email.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.EMAIL,
                    AppManager.getString(R.string.err_email_empty)
                )
            )
            return
        }
        if (!Utils.isValidEmail(email.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.EMAIL,
                    AppManager.getString(R.string.err_email_invalid)
                )
            )
            return
        }

        if (TextUtils.isEmpty(mobileNumber.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PHONE,
                    AppManager.getString(R.string.err_phone_empty)
                )
            )
            return
        }

        if(!Utils.isValidPhoneNumber(mobileNumber.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.PHONE,
                    AppManager.getString(R.string.err_mobile_min_chars)
                )
            )
            return
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


        val map = HashMap<String, String>()
        map["username"] = userName.trim()
        map["email"] = email.trim()
        map["mobile"] = mobileNumber.trim()
        map["company_name"] = company.trim()
        map["pan_no"] = panNumber.trim()
        map["gst_no"] = gst.trim()
        map["password"] = password.trim()
        map["method_name"] = "user_registration"
        map["seller_type"] = "customer"

        requestRegister(map)
    }

    fun requestRegister(params: HashMap<String, String>) {
        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<User>> {
                        override val deferred: Deferred<BaseResponse<User>>
                            get() = ApiClient.apiService.register(
                                params
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.USER_REGISTER
                        override val repoListener: RepoListener
                            get() = this@SignUpViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        AppManager.setUser(this.response.data)
                        onRegisterSuccessful.value = SingleActionEvent(this.response.message)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


    fun goToLogin() {
        closeKeyBoard()
        navigateToLogin.value = SingleActionEvent(true)
    }
}