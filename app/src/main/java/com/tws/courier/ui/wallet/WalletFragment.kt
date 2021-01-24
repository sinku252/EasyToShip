package com.tws.courier.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.FragmentBookingSuccessBinding
import com.tws.courier.databinding.FragmentWalletBinding
import com.tws.courier.domain.dialogs.CustomDialogClass
import com.tws.courier.showAddMoneyDialog
import com.tws.courier.showToastShort
import com.tws.courier.ui.booking_success.BookingViewModel
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.notification.NotificationFragment
import com.tws.courier.ui.notification.NotificationViewModel
import kotlinx.android.synthetic.main.activity_booking_successful.view.*
import kotlinx.android.synthetic.main.dialog_add_money.view.*

class WalletFragment : HomeBaseFragment<WalletViewModel, FragmentWalletBinding>()
{
    companion object {
        val TAG = "WalletFragment"
        fun newInstance() = WalletFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.wallet)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_wallet
    override fun getViewModelClass(): Class<WalletViewModel> = WalletViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

    }

    private fun observeLiveData()
    {
        viewBinding.user= AppManager.getUser()

        viewModel.onAddMoneyDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
              //  activity?.let { it1 -> CustomDialogClass(it1).show() }
                /*activity?.showAddMoneyDialog {
                    fragmentListener?.navigateToPaymentActivity("100")
                }*/
                showMessageBox()
            }
        })
    }

    fun showMessageBox(){

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.dialog_add_money, null)

        //AlertDialogBuilder
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }

        //setting text values
     /*   messageBoxView.message_box_header.text = "This is message header"
        messageBoxView.message_box_content.text = "This is message content"*/

        //show dialog
        val  messageBoxInstance = messageBoxBuilder?.show()

        //set Listener
        messageBoxView.add_money.setOnClickListener(){
            //close dialog
            fragmentListener?.navigateToPaymentActivity("100")
            messageBoxInstance?.dismiss()
        }
    }


}