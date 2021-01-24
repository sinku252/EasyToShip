package com.tws.courier.ui.create_shipment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shuhart.stepview.StepView.OnStepClickListener
import com.tws.courier.*
import com.tws.courier.databinding.FragmentCreateShipmentBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.models.CreateOrder
import com.tws.courier.domain.models.OrderCalculation
import com.tws.courier.domain.models.OrderPrice
import com.tws.courier.ui.home.base.HomeBaseFragment


class CreateShipmentFragment : HomeBaseFragment<CreateShipmentViewModel, FragmentCreateShipmentBinding>()
{
    companion object {
        val TAG = "CreateShipmentFragment"
        fun newInstance() = CreateShipmentFragment()


    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.createshipment)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_create_shipment
    override fun getViewModelClass(): Class<CreateShipmentViewModel> = CreateShipmentViewModel::class.java

   // var createOrder= CreateOrder("sfbkf")

    var orderCalculation: OrderCalculation? = null
    lateinit var orderPrice: OrderPrice
    var createOrder = CreateOrder()
    var typeAddress:Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        initView()
        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        model.addressData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(typeAddress)
                {

                    createOrder.pickupAddress = it.name+","+it.houseNo+""+it.address+","+it.city+","+it.state
                    viewBinding.createOrder = createOrder
                }
                else
                {
                    createOrder.deliveryAddress = it.id
                    viewBinding.createOrder = createOrder

                    viewBinding.cvAddDeliveryAddress.visibility=View.GONE
                    viewBinding.layoutAddress.visibility=View.VISIBLE
                    viewBinding.rowAddress.setVariable(BR.item, it)
                    viewBinding.rowAddress.radio1.isChecked=true
                    viewBinding.rowAddress.ivEditDelete.setImageResource(R.drawable.edit_address)

                    viewBinding.rowAddress.ivEditDelete.setOnClickListener {
                        fragmentListener?.navigateToListAddress(false)
                    }

                }
            }
        })

        /*model.addressData.observe(viewLifecycleOwner, Observer {
            Log.e("fasf",""+typeAddress)
        })*/
    }

    fun initView()
    {

        viewBinding.stepView.setOnStepClickListener(OnStepClickListener {
            // 0 is the first step
            if (it == 0) {

                viewBinding.llShipmentType.visibleOrGone = true
                viewBinding.llShipmentArea.visibleOrGone = false
                viewBinding.llShipmentVhicleType.visibleOrGone = false
                viewBinding.llShipmentAddressDimension.visibleOrGone = false
                viewBinding.llShipmentAdditinalDetail.visibleOrGone = false
            } else if (it == 1) {
                viewBinding.llShipmentType.visibleOrGone = false
                viewBinding.llShipmentArea.visibleOrGone = true
                viewBinding.llShipmentVhicleType.visibleOrGone = false
                viewBinding.llShipmentAddressDimension.visibleOrGone = false
                viewBinding.llShipmentAdditinalDetail.visibleOrGone = false
            } else if (it == 2) {
                if (!viewBinding.createOrder?.areaType.equals("")) {
                    viewBinding.llShipmentType.visibleOrGone = false
                    viewBinding.llShipmentArea.visibleOrGone = false
                    viewBinding.llShipmentVhicleType.visibleOrGone = true
                    viewBinding.llShipmentAddressDimension.visibleOrGone = false
                    viewBinding.llShipmentAdditinalDetail.visibleOrGone = false
                } else {
                    showToastShort = "Select Area Type First"
                }

            } else if (it == 3) {
                viewBinding.llShipmentType.visibleOrGone = false
                viewBinding.llShipmentArea.visibleOrGone = false
                viewBinding.llShipmentVhicleType.visibleOrGone = false
                viewBinding.llShipmentAddressDimension.visibleOrGone = true
                viewBinding.llShipmentAdditinalDetail.visibleOrGone = false
            }
        })

        viewBinding.btText.setOnClickListener {

            if(viewBinding.llShipmentAddressDimension.visible)
            {
                viewModel.checkOrder(viewBinding.createOrder!!)
            }
            else if(viewBinding.btText.text.equals("View Price"))
            {
                viewModel.getOrderPrice(viewBinding.createOrder!!)
            }
            else if(viewBinding.btText.text.equals("Place Order"))
            {
                viewModel.placeOrder(viewBinding.createOrder!!, viewBinding.orderPrice!!)
            }

        }
    }


    private fun observeLiveData() {


        viewBinding.createOrder= createOrder

        createOrder.weight = "1"
        viewBinding.createOrder = createOrder

        viewModel.llShipmentArea.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {

                if (it.view.id == viewBinding.llDocument.id) {
                    createOrder.shipmentType = "document"
                }
                if (it.view.id == viewBinding.llParcel.id) {
                    createOrder.shipmentType = "parcel"
                }
                viewBinding.createOrder = createOrder
                viewBinding.stepView.go(1, true)
                viewBinding.llShipmentType.visibility = View.GONE
                viewBinding.llShipmentArea.visibility = View.VISIBLE
                /*if (it) {
                   // order.shipmentType=

                }*/
            }
        })

        viewModel.changeInWeight.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                createOrder.weight = it
                viewBinding.createOrder = createOrder
            }
        })



        viewModel.onPorterSelect.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(it)
                {
                    viewBinding.tlPorter.visibility=View.VISIBLE
                }
                else
                {
                    viewBinding.tlPorter.visibility=View.GONE
                    viewBinding.etPorter.setText("")
                }

            }
        })

        viewModel.llShipmentVhicleType.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.view.id == viewBinding.llLocal.id) {
                    createOrder.areaType = "local"
                } else if (it.view.id == viewBinding.llNational.id) {
                    createOrder.areaType = "national"
                } else if (it.view.id == viewBinding.llInternational.id) {
                    createOrder.areaType = "international"
                }

                viewBinding.createOrder = createOrder
                viewBinding.stepView.go(2, true)
                viewBinding.llShipmentArea.visibility = View.GONE
                viewBinding.llShipmentVhicleType.visibility = View.VISIBLE

/*                if (it) {

                }*/
            }
        })

        viewModel.llShipmentAddressDimension.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.view.id == viewBinding.llBike.id) {
                    createOrder.vehicleType = "bike"
                } else if (it.view.id == viewBinding.llTruck.id) {
                    createOrder.vehicleType = "truck"
                } else if (it.view.id == viewBinding.llTrain.id) {
                    createOrder.vehicleType = "train"
                } else if (it.view.id == viewBinding.llFlight.id) {
                    createOrder.vehicleType = "flight"
                }
                viewBinding.createOrder = createOrder
                viewBinding.stepView.go(3, true)
                viewBinding.llShipmentVhicleType.visibility = View.GONE
                viewBinding.llShipmentAddressDimension.visibility = View.VISIBLE
                viewBinding.btContinue.visibility = View.VISIBLE

            }
        })

        viewModel.onOrderSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = "Orders successfully"
                //   fragmentListener?.navigateToDashboardFragment()
                fragmentListener?.navigateToBookingSuccessFragment(it)
            }
        })

        viewModel.radio_checked.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.rbDoorDelivery.id == it) {
                    createOrder.doorDelivery = "1"
                } else if (viewBinding.rbSelfPickup.id == it) {
                    createOrder.selfPickup = "1"
                }
                //transmission speen
                else if (viewBinding.rbGold.id == it) {
                    createOrder.transmissionSpeed = "gold"
                } else if (viewBinding.rbSilver.id == it) {
                    createOrder.transmissionSpeed = "silver"
                } else if (viewBinding.rbPlatinum.id == it) {
                    createOrder.transmissionSpeed = "platinum"
                } else if (viewBinding.rbWallet.id == it) {
                    createOrder.paymentMode = "wallet"
                } else if (viewBinding.rbCod.id == it) {
                    createOrder.paymentMode = "cod"
                } else if (viewBinding.rbCop.id == it) {
                    createOrder.paymentMode = "cop"
                }
                viewBinding.createOrder = createOrder
            }

        })

        viewModel.navigateToAddress.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Log.e("saf",""+it)
                if(it)
                {
                    typeAddress=true
                    fragmentListener?.navigateToListAddress(true)
                }
                else
                {
                    typeAddress=false
                    fragmentListener?.navigateToListAddress(false)
                }


              /*  startActivityForResult(activity?.applicationContext?.let { it1 ->
                    AddressList.createIntentLauncher(it1, "pickup")
                }, 10)*/
            }
        })

        viewModel.checkVelidation.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (!it) {
                    viewBinding.stepView.go(4, true)
                    viewBinding.btText.text = "View Price"
                    viewBinding.llShipmentAddressDimension.visibility = View.GONE
                    viewBinding.llShipmentAdditinalDetail.visibility = View.VISIBLE
                }
            }
        })

        viewModel.onOrderPriceSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                viewBinding.llPrice.visibility = View.VISIBLE
                viewBinding.llPaymentMode.visibility = View.VISIBLE
                viewBinding.btText.text = "Place Order"
                viewBinding.orderPrice = it
                orderPrice = it
            }
        })

      /*  viewModel.onAddressListReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                viewBinding.recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                viewBinding.recyclerView.adapter = AddressAdapter(
                    object : AddressAdapter.AdapterCallbacks {
                        override fun onSelectClicked(address: Address) {
                            createOrder.deliveryAddress = address.id
                            viewBinding.createOrder = createOrder
                        }

                        override fun onEditDeleteClicked(address: Address) {

                        }

                    }
                ).apply {
                    setList(ArrayList(it))
                    isEdit = true
                }

            }
        })
*/


    }


    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.MESSAGE -> {
                showMessageDialog(inputError.message)
            }
          /*  InputErrorType.PASSWORD -> {
                showMessageDialog(inputError.message)
            }*/
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == 20) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK)
            {
                /*var returnString = data!!.getStringExtra("address")
                var gson = Gson()
                var address:Address=gson.fromJson(returnString, com.stripe.android.model.Address.class));*/

            }
        }
    }



}