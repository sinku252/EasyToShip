package com.tws.courier.ui.signup

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.tws.courier.ui.home.base.HomeBaseFragment

import com.tws.courier.R
import com.tws.courier.databinding.FragmentSignUpBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.showToastShort


class SignUpFragment  : HomeBaseFragment<SignUpViewModel, FragmentSignUpBinding>()  {
    companion object {
        fun newInstance() = SignUpFragment()
    }


    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_sign_up
    override fun getViewModelClass(): Class<SignUpViewModel> = SignUpViewModel::class.java

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

        viewModel.onRegisterSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
                mPreference!!.isLogin=true
                fragmentListener?.navigateToDashboardFragment()
            }
        })
    }

    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.EMAIL -> {
                //viewBinding.loginMobile.requestFocus()
                showMessageDialog(inputError.message)
            }
            InputErrorType.PHONE -> {
                //viewBinding.loginPassword.requestFocus()
                showMessageDialog(inputError.message)
            }
            InputErrorType.MESSAGE -> {
                //viewBinding.loginPassword.requestFocus()
                showMessageDialog(inputError.message)
            }
            InputErrorType.PASSWORD -> {
                //viewBinding.loginPassword.requestFocus()
                showMessageDialog(inputError.message)
            }
        }
    }

}