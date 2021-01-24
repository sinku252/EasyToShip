package com.tws.courier.ui.account_setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.monrotv.ui.dashboard.adapters.GenericAdapter
import com.tws.courier.*
import com.tws.courier.databinding.FragmentAcountSettingBinding
import com.tws.courier.domain.models.Address
import com.tws.courier.ui.create_shipment.adapter.AddressAdapter
import com.tws.courier.ui.home.base.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_acount_setting.*


class AccountSettingFragment : HomeBaseFragment<AccountSettingViewModel, FragmentAcountSettingBinding>(),View.OnClickListener
{
    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.accountsetting)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_acount_setting
    override fun getViewModelClass(): Class<AccountSettingViewModel> = AccountSettingViewModel::class.java


    val adapter = GenericAdapter(
        R.layout.row_address,
        object : GenericAdapter.AdapterCallbacks<Address> {
            override fun onItemClicked(item: Address) {
               // fragmentListener?.navigateToMediaDetailsFragment(item)
            }
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        showView(1)
        initViews()

       // viewBinding.address
        //viewBinding.a

    }

    private fun observeLiveData() {

        viewModel.showView.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showView(it)
            }
        })

        viewModel.generalDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {

                viewBinding.user=it
            }
        })

        viewModel.bankDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                viewBinding.bank=it
            }
        })

        viewModel.onAddressListReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                /*viewBinding.layoutAddress.rvAddressList.layoutManager = LinearLayoutManager(context)
                viewBinding.layoutAddress.rvAddressList.adapter = adapter*/

                viewBinding.layoutAddress.rvAddressList.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                viewBinding.layoutAddress.rvAddressList.adapter = AddressAdapter(
                    object : AddressAdapter.AdapterCallbacks {
                        override fun onSelectClicked(address: Address)
                        {
                           /* createOrder.deliveryAddress = address.id
                            viewBinding.createOrder=createOrder*/
                        }

                        override fun onEditDeleteClicked(address: Address)
                        {
                            viewModel.deleteAddress(address)
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

        viewModel.updateGeneralDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
            }
        })

        viewModel.updateBankDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
            }
        })
    }

    private fun showView(type:Int)
    {

        if(type==1)
        {
            layout_address.visibility=View.GONE
            layout_accounting.visibility=View.GONE
            layout_general.visibility=View.VISIBLE
            viewBinding.generalDetailsIndicator.visible=true
            viewBinding.addressDetailsIndicator.visible=false
            viewBinding.accountingDetailsIndicator.visible=false
            viewBinding.nextButton.visibility=View.VISIBLE
            AppManager.getUser()?.let { user ->
                viewModel.getGeneral()
            }

        }
        else if(type==2)
        {
            layout_general.visibility=View.GONE
            layout_accounting.visibility=View.GONE
            layout_address.visibility=View.VISIBLE
            viewBinding.generalDetailsIndicator.visible=false
            viewBinding.addressDetailsIndicator.visible=true
            viewBinding.accountingDetailsIndicator.visible=false

            viewBinding.nextButton.visibility=View.GONE
            AppManager.getUser()?.let { user ->
                viewModel.requestAddress(user.id)
            }

        }
        else if(type==3)
        {
            layout_general.visibility=View.GONE
            layout_address.visibility=View.GONE
            layout_accounting.visibility=View.VISIBLE
            viewBinding.generalDetailsIndicator.visible=false
            viewBinding.addressDetailsIndicator.visible=false
            viewBinding.accountingDetailsIndicator.visible=true
            viewBinding.nextButton.visibility=View.VISIBLE
            viewModel.getBank()
        }

    }


    companion object {
        val TAG = "AccountSettingFragment"
        fun newInstance() = AccountSettingFragment()
    }


    private fun setAddressToviews(list: List<Address>) {
        if (list.isNotEmpty()) {
            adapter.addItems(list)
            viewBinding.layoutAddress.textEmpty.visibility = View.GONE
        } else viewBinding.layoutAddress.textEmpty.visibility = View.VISIBLE
    }

    private fun initViews() {


        viewBinding.nextButton.setOnClickListener(this)
        }


    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.next_button -> {
                if(layout_general.visible)
                {
                    viewModel.updateGeneralDetails(viewBinding.user)
                }
                else if(layout_accounting.visible)
                {
                    viewModel.updateBankDetails(viewBinding.bank)
                }
            }
        }
    }

}


