package com.tws.courier.ui.address

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.databinding.FragmentAcountSettingBinding
import com.tws.courier.databinding.FragmentAddressBinding
import com.tws.courier.domain.utils.KotlinPreferencesHelper
import com.tws.courier.showToastShort
import com.tws.courier.ui.account_setting.AccountSettingViewModel
import com.tws.courier.ui.create_shipment.CreateShipmentFragment
import com.tws.courier.ui.create_shipment.SharedViewModel
import com.tws.courier.ui.home.base.HomeBaseFragment


class AddressFragment : HomeBaseFragment<AddressViewModel, FragmentAddressBinding>()
{
    companion object {
        val TAG = "AddressFragment"
        fun newInstance() = AddressFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.myaddress)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_address
    override fun getViewModelClass(): Class<AddressViewModel> = AddressViewModel::class.java

    lateinit var model: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

    }

    private fun observeLiveData() {
        viewModel.onAddressSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
                model.refreshAddressList()
                activity?.onBackPressed()

                /*mPreference!!.isLogin=true
                fragmentListener?.navigateToDashboardFragment()*/
            }
        })

        viewModel.radio_checked.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.rbPickup.id == it) {
                    viewBinding.viewmodel?.addressType="pickup"
                }
                else if(viewBinding.rbDelivery.id == it)
                {
                    viewBinding.viewmodel?.addressType="delivery"
                }
            }

        })
    }

}