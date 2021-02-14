package com.tws.courier.ui.booking_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.annotations.SerializedName
import com.tws.courier.R
import com.tws.courier.databinding.FragmentBookingSuccessBinding
import com.tws.courier.domain.models.OrderPrice
import com.tws.courier.domain.models.OrderSuccess

import com.tws.courier.getDateWithMonthName
import com.tws.courier.getDateWithMonthNameFromCalendar
import com.tws.courier.ui.home.base.HomeBaseFragment
import kotlinx.android.synthetic.main.common_input_dialog.view.*
import kotlinx.android.synthetic.main.price_dialog.view.*

class BookingSuccessful : HomeBaseFragment<BookingViewModel, FragmentBookingSuccessBinding>()
{
    /*companion object {
        val TAG = "BookingSuccessful"
        fun newInstance() = BookingSuccessful()
    }*/
    companion object {
        val TAG = "BookingSuccessful"
        val ARG_ORDER_SUCCESS = "order"

        fun newInstance(orderSuccess: OrderSuccess) = BookingSuccessful().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ORDER_SUCCESS, orderSuccess)
            }
        }
    }

    private var orderSuccess: OrderSuccess? = null

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.booking_success)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_booking_success
    override fun getViewModelClass(): Class<BookingViewModel> = BookingViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ORDER_SUCCESS)) orderSuccess = it.getParcelable(ARG_ORDER_SUCCESS)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

    }

    private fun observeLiveData() {
        viewBinding.orderSuccess=orderSuccess
        viewBinding.tvDateTime.text= getDateWithMonthName(viewBinding.orderSuccess?.reqCO?.schedule!!)

        viewModel.showPriceDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(it)
                {
                  /*  var totalAmount= orderSuccess?.totalAmount
                    var totalDeliveryChallan=orderSuccess?.totalDeliveryChallan
                    var totalFair=""
                    var totalGstCharge=orderSuccess?.totalGstCharge
                    var totalInsuranceCharge=orderSuccess?.totalInsuranceCharge
                    var totalPeakCharge=orderSuccess?.totalPeakCharge
                    var totalPorterCharge=orderSuccess?.totalPorterCharge
                    var totalReverseTrip=orderSuccess?.totalReverseTrip
                    var totalOtherCharge=orderSuccess?.totalOtherCharge
                    var totalDistancePrice=orderSuccess?.totalDistancePrice
                    var totalWieghtPrice=""*/

                    orderSuccess?.let{
                        var totalAmount= it.totalAmount
                        var totalDeliveryChallan=it.totalDeliveryChallan
                        var totalFair=""
                        var totalGstCharge=it.totalGstCharge
                        var totalInsuranceCharge=it.totalInsuranceCharge
                        var totalPeakCharge=it.totalPeakCharge
                        var totalPorterCharge=it.totalPorterCharge
                        var totalReverseTrip=it.totalReverseTrip
                        var totalOtherCharge=it.totalOtherCharge
                        var totalDistancePrice=it.totalDistancePrice
                        var totalWieghtPrice=""
                        var orderPrice:OrderPrice=OrderPrice(totalAmount,
                            totalDeliveryChallan,
                            "",
                            totalDistancePrice,
                            totalFair,
                            totalGstCharge,
                            totalInsuranceCharge,
                            totalOtherCharge,
                            totalPeakCharge,
                            totalPorterCharge,
                            totalReverseTrip,
                            null,
                            totalWieghtPrice,
                            null)




                        /*var orderPrice:OrderPrice=OrderPrice(totalAmount,
                            totalDeliveryChallan,
                            totalFair,
                            totalGstCharge,
                            totalInsuranceCharge,
                            totalPeakCharge,
                            totalPorterCharge,
                            totalReverseTrip,
                            totalOtherCharge,
                            totalDistancePrice,
                            totalWieghtPrice)*/
                        showPriceBox(orderPrice)
                    }


                }

            }
        })


     /*   if(orderSuccess?.vehicleType.equals("bike"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_bike_blue)
        else if(orderSuccess?.vehicleType.equals("truck"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_truck_blue)
        else if(orderSuccess?.vehicleType.equals("train"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_train_blue)
        else if(orderSuccess?.vehicleType.equals("flight"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_flight_blue)*/

    }



    fun showPriceBox(orderPrice: OrderPrice) {

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.price_dialog,
            null)
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }
        val  messageBoxInstance = messageBoxBuilder?.show()
        messageBoxView.tv_order_fair.text=orderPrice.totalFair
        messageBoxView.tv_order_insurance.text=orderPrice.totalInsuranceCharge
        messageBoxView.tv_order_porter.text=orderPrice.totalPorterCharge
        messageBoxView.tv_order_reverse.text=orderPrice.totalReverseTrip
        messageBoxView.tv_order_delivery_challan.text=orderPrice.totalDeliveryChallan
        messageBoxView.tv_order_gst.text=orderPrice.totalGstCharge
        messageBoxView.tv_order_peak.text=orderPrice.totalPeakCharge
        messageBoxView.tv_order_total.text=getString(R.string.rs,orderPrice.totalAmountPrice)

        messageBoxView.price_dialog_ok.setOnClickListener(){
            messageBoxInstance?.dismiss()
        }

    }

}