package com.tws.courier.ui.booking_success

import androidx.lifecycle.MutableLiveData
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent

class BookingViewModel:BaseViewModel()
{
    val showPriceDialog = MutableLiveData<SingleActionEvent<Boolean>>()

    fun viewFair()
    {
        showPriceDialog.value = SingleActionEvent(true)
    }
}