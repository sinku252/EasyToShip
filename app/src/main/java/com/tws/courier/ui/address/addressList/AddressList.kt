package com.tws.courier.ui.address.addressList

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.AddressListMainBinding
import com.tws.courier.databinding.FragmentAddressBinding
import com.tws.courier.domain.models.Address
import com.tws.courier.ui.address.AddressFragment
import com.tws.courier.ui.address.AddressViewModel
import com.tws.courier.ui.create_shipment.SharedViewModel
import com.tws.courier.ui.create_shipment.adapter.AddressAdapter
import com.tws.courier.ui.home.base.HomeBaseFragment

class AddressList  : HomeBaseFragment<AddressListViewModel, AddressListMainBinding>()
{
    companion object {
        val TAG = "AddressList"
        //fun newInstance() = AddressList()
        val ARG_TYPE = "type"

        fun newInstance(type: Boolean) = AddressList().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_TYPE, type)
            }
        }
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
    override fun getLayoutResource(): Int = R.layout.address_list_main
    override fun getViewModelClass(): Class<AddressListViewModel> = AddressListViewModel::class.java

    lateinit var model: SharedViewModel
    private var type : Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        AppManager.getUser()?.let { user ->
            type?.let { viewModel.requestAddress(it) }
        }

        model.refreshAddressData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                AppManager.getUser()?.let { user ->
                    type?.let { viewModel.requestAddress(it) }
                }
            }
        })


    }

    private fun observeLiveData() {

        viewModel.onAddressListReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                /*viewBinding.layoutAddress.rvAddressList.layoutManager = LinearLayoutManager(context)
                viewBinding.layoutAddress.rvAddressList.adapter = adapter*/


                viewBinding.rvAddressList.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                viewBinding.rvAddressList.adapter = AddressAdapter(
                    object : AddressAdapter.AdapterCallbacks {
                        override fun onSelectClicked(address: Address)
                        {
                            /*type?.let { it1 -> model.sendAddress(address) }*/
                            model.sendAddress(address)
                            activity?.onBackPressed()
                            /* createOrder.deliveryAddress = address.id
                             viewBinding.createOrder=createOrder*/
                        }

                        override fun onEditDeleteClicked(address: Address)
                        {
                            //viewModel.deleteAddress(address)
                            model.refreshAddressList()
                        }

                    }
                ).apply {
                    setList(ArrayList(it))
                    isEdit=false
                }
                /*  val listAlt: ArrayList<Address> = ArrayList()
                  val list = ArrayList(it)
                  list.forEach { mo ->
                      listAlt.add(mo)
                  }
                  list.clear()
                  list.addAll(listAlt)
                  setAddressToviews(list)*/
            }
        })

        viewModel.navigateToAddress.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToAddNewAddress()
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_TYPE)) type = it.getBoolean(ARG_TYPE)
        }
    }



}