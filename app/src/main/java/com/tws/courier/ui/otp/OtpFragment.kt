package com.tws.courier.ui.otp

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.tws.courier.R
import com.tws.courier.databinding.FragmentOtpBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.models.ForgotPasswordResponse
import com.tws.courier.ui.home.base.HomeBaseFragment


class OtpFragment  : HomeBaseFragment<OtpViewModel, FragmentOtpBinding>()
{
    companion object {
        val TAG = "OtpFragment"
        val ARG_USER_ID = "user_id"
        val ARG_OTP = "otp"

        // fun newInstance(status: Int) = OrdersFragment()

       /* fun newInstance(email: Int) = OrdersFragment().apply {
            arguments = Bundle().apply {
                putInt(forgot_password_response, email)
            }
        }*/

        fun newInstance(userId: String, genreTitle: String) = OtpFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_USER_ID, userId)
                putString(ARG_OTP, genreTitle)
            }
        }

    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_otp
    override fun getViewModelClass(): Class<OtpViewModel> = OtpViewModel::class.java

    private var userId: String=""
    private var otp: String=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    private fun initView() {

        viewBinding.btVerify.setOnClickListener {
            viewModel.verifyOtp(otp)
        }
    }

    private fun observeLiveData() {

        viewModel.onOtpVerify.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(it)
                {
                    fragmentListener?.navigateToChangePasswordFragment(userId,otp)
                }
            }
        })
    }

    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.OTP_EMAIL -> {
                viewBinding.etOtp.requestFocus()
                showMessageDialog(inputError.message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ARG_USER_ID = "user_id"
        val ARG_OTP = "otp"
        arguments?.let {
            if (it.containsKey(ARG_USER_ID)) userId = it.getString(ARG_USER_ID).toString()
            if (it.containsKey(ARG_OTP)) otp = it.getString(ARG_OTP).toString()
        }
    }
}