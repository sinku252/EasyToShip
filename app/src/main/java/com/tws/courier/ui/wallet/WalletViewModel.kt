package com.tws.courier.ui.wallet

import androidx.lifecycle.MutableLiveData
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent

class WalletViewModel : BaseViewModel()
{
    val onAddMoneyDialog = MutableLiveData<SingleActionEvent<Boolean>>()

    fun callAddMoneyDialog()
    {
        onAddMoneyDialog.value = SingleActionEvent(true)
    }

}