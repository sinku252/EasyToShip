package com.tws.courier.ui.onboarding

import androidx.lifecycle.MutableLiveData
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent

class OnboardingViewModel  : BaseViewModel()
{
    val navigateToLogin = MutableLiveData<SingleActionEvent<Boolean>>()

    fun goToLogin() {
        closeKeyBoard()
        navigateToLogin.value = SingleActionEvent(true)
    }
}