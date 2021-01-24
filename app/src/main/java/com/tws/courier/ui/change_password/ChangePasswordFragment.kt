package com.tws.courier.ui.change_password

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.tws.courier.R
import com.tws.courier.databinding.FragmentChangePasswordBinding
import com.tws.courier.databinding.FragmentOtpBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.showToastShort
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.otp.OtpFragment
import com.tws.courier.ui.otp.OtpViewModel

class ChangePasswordFragment : HomeBaseFragment<ChangePasswordViewModel, FragmentChangePasswordBinding>()
{
    companion object {
        val TAG = "ChangePasswordFragment"
        //fun newInstance() = ChangePasswordFragment()
        fun newInstance(userId: String, genreTitle: String) = ChangePasswordFragment().apply {
            arguments = Bundle().apply {
                putString(OtpFragment.ARG_USER_ID, userId)
                putString(OtpFragment.ARG_OTP, genreTitle)
            }
        }
    }

    private var userId: String=""
    private var otp: String=""


    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_change_password
    override fun getViewModelClass(): Class<ChangePasswordViewModel> = ChangePasswordViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    private fun initView() {

        viewBinding.btChangePassword.setOnClickListener {
            viewModel.changePassword(userId)
        }
    }

    private fun observeLiveData() {

        viewModel.onChangePasswordSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(it)
                {
                    showToastShort ="Password Updated Login to Continue"
                    fragmentListener?.navigateToLoginFragment()
                }
            }
        })
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

    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.PASSWORD -> {
                viewBinding.editPassword.requestFocus()
                showToast(inputError.message)
            }
            InputErrorType.CONFIRM_PASSWORD -> {
                viewBinding.editConfirmPassword.requestFocus()
                showToast(inputError.message)
            }
        }
    }
}