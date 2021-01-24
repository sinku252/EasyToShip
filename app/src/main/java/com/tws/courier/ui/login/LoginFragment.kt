package com.tws.courier.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.R
import com.tws.courier.databinding.FragmentLoginBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.showToastShort

class LoginFragment : HomeBaseFragment<LoginViewModel, FragmentLoginBinding>() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_login
    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewBinding.imageBack.imageLocalDefault = R.drawable.common_bg
        observeLiveData()

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = Color.TRANSPARENT
        }*/

        viewBinding.loginPassword.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_GO) {
                viewModel.login()
                true
            }
            false
        }

//        viewBinding.editEmail.setText("sherman.belleh@hotmail.com")
//        viewBinding.editPassword.setText("monro@1807")
//        viewBinding.editEmail.setText("sar00@yopmail.com")
//        viewBinding.editPassword.setText("123456")
    }


    private fun observeLiveData() {
        viewModel.onLoginSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
                mPreference!!.isLogin=true
                fragmentListener?.navigateToDashboardFragment()
            }
        })
        viewModel.navigateToForgotPassword.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToForgotPasswordFragment()
            }
        })
        viewModel.navigateToRegister.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToRegisterFragment()
            }
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToDashboardFragment()
            }
        })
    }



    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.EMAIL -> {
                viewBinding.loginMobile.requestFocus()
                showMessageDialog(inputError.message)
            }
            InputErrorType.PHONE -> {
                viewBinding.loginMobile.requestFocus()
                showMessageDialog(inputError.message)
            }
            InputErrorType.PASSWORD -> {
                viewBinding.loginPassword.requestFocus()
                showMessageDialog(inputError.message)
            }
        }
    }
}