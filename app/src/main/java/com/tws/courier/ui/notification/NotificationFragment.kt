package com.tws.courier.ui.notification

import android.os.Bundle
import android.view.View
import com.tws.courier.R
import com.tws.courier.databinding.FragmentAddressBinding
import com.tws.courier.databinding.FragmentNotificationListBinding
import com.tws.courier.showToastShort
import com.tws.courier.ui.address.AddressFragment
import com.tws.courier.ui.address.AddressViewModel
import com.tws.courier.ui.home.base.HomeBaseFragment

class NotificationFragment : HomeBaseFragment<NotificationViewModel, FragmentNotificationListBinding>()
{
    companion object {
        val TAG = "NotificationFragment"
        fun newInstance() = NotificationFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.notification)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_notification_list
    override fun getViewModelClass(): Class<NotificationViewModel> = NotificationViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {
    }
}