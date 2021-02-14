package com.tws.courier.ui.help

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tws.courier.R
import com.tws.courier.databinding.FragmentAddressBinding
import com.tws.courier.databinding.FragmentHelpBinding
import com.tws.courier.domain.models.Address
import com.tws.courier.domain.models.Help
import com.tws.courier.domain.models.Order
import com.tws.courier.domain.models.OrderSuccess
import com.tws.courier.ui.address.AddressViewModel
import com.tws.courier.ui.create_shipment.adapter.AddressAdapter
import com.tws.courier.ui.help.adapter.HelpAdapter
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.notification.NotificationFragment
import com.tws.courier.ui.notification.NotificationViewModel
import com.tws.courier.ui.orders.adapter.OrderAdapter

class HelpFragment : HomeBaseFragment<HelpViewModel, FragmentHelpBinding>()
{
    companion object {
        val TAG = "HelpFragment"
        fun newInstance() = HelpFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.help)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_help
    override fun getViewModelClass(): Class<HelpViewModel> = HelpViewModel::class.java

    val adapter = HelpAdapter(
        object : HelpAdapter.AdapterCallbacks {
            override fun onSelectClicked(help: Help) {
                    fragmentListener?.navigateToTokenResponseFragment(help)
            }
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    private fun initView()
    {
        viewBinding.rvTickets.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.rvTickets.adapter =adapter
    }

    private fun observeLiveData()
    {
        /*viewModel.navigateToChat.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToChatFragment()
            }
        })

        viewModel.navigateToToken.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToTokenFragment()
            }
        })*/
        viewModel.onTicketListReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                adapter.setList(ArrayList(it))
            }
        })
    }
}