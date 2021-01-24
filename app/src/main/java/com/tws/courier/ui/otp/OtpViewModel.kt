package com.tws.courier.ui.otp

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.base.BaseFragment
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent

class OtpViewModel : BaseViewModel()
{
    val onOtpVerify = MutableLiveData<SingleActionEvent<Boolean>>()

    var otp: String = ""

    fun verifyOtp(actualOtp:String)
    {
        closeKeyBoard()
        if (TextUtils.isEmpty(otp.trim())) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.OTP_EMAIL,
                    AppManager.getString(R.string.err_otp_empty)
                )
            )
            return
        }
        if (actualOtp!=otp) {
            showInputError.value = SingleActionEvent(
                BaseFragment.InputError(
                    InputErrorType.OTP_EMAIL,
                    AppManager.getString(R.string.err_otp_invalid)
                )
            )
            return
        }
        onOtpVerify.value = SingleActionEvent(true)

    }

}