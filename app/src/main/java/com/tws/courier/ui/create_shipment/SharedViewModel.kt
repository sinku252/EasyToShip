package com.tws.courier.ui.create_shipment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.models.Address

class SharedViewModel : ViewModel() {

    val addressData = MutableLiveData<SingleActionEvent<Address>>()
    val refreshAddressData = MutableLiveData<SingleActionEvent<Boolean>>()

    fun sendAddress(address: Address) {
        addressData.value = SingleActionEvent(address)
    }

    fun refreshAddressList() {
        refreshAddressData.value = SingleActionEvent(true)
    }
}