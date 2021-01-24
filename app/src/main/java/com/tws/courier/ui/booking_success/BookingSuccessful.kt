package com.tws.courier.ui.booking_success

import android.os.Bundle
import android.view.View
import com.tws.courier.R
import com.tws.courier.databinding.FragmentBookingSuccessBinding
import com.tws.courier.domain.models.OrderSuccess
import com.tws.courier.getDateWithMonthName
import com.tws.courier.getDateWithMonthNameFromCalendar
import com.tws.courier.ui.home.base.HomeBaseFragment

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
     /*   if(orderSuccess?.vehicleType.equals("bike"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_bike_blue)
        else if(orderSuccess?.vehicleType.equals("truck"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_truck_blue)
        else if(orderSuccess?.vehicleType.equals("train"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_train_blue)
        else if(orderSuccess?.vehicleType.equals("flight"))
            viewBinding.ivVehicle.setImageResource(R.drawable.ic_flight_blue)*/

    }


}