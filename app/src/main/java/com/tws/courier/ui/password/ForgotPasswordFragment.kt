package com.tws.courier.ui.password

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.tws.courier.R
import com.tws.courier.databinding.FragmentForgotPasswordBinding
import com.tws.courier.showToastShort
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.signup.SignUpViewModel

class ForgotPasswordFragment : HomeBaseFragment<ForgotPasswordViewModel, FragmentForgotPasswordBinding>()
{
    companion object {
        val TAG = "ForgotPassword"
        fun newInstance() = ForgotPasswordFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_forgot_password
    override fun getViewModelClass(): Class<ForgotPasswordViewModel> = ForgotPasswordViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToLoginFragment()
            }
        })

        viewModel.navigateToRegister.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToRegisterFragment()
            }
        })

        viewModel.onForgetMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
                // fragmentListener?.navigateToDashboardFragment()
            }
        })

        viewModel.onForgetSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //showToastShort = it
                fragmentListener?.navigateToOtpFragment(it)
               // fragmentListener?.navigateToDashboardFragment()
            }
        })
    }
}