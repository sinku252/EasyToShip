package com.tws.courier.ui.token

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.tws.courier.R

import com.tws.courier.databinding.FragmentTokenBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.models.Chat
import com.tws.courier.showToastShort
import com.tws.courier.ui.help.HelpFragment
import com.tws.courier.ui.help.HelpViewModel
import com.tws.courier.ui.home.base.HomeBaseFragment

class TokenFragment : HomeBaseFragment<TokenViewModel, FragmentTokenBinding>()
{
    companion object {
        val TAG = "TokenFragment"
        fun newInstance() = TokenFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.token)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_token
    override fun getViewModelClass(): Class<TokenViewModel> = TokenViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData()
    {
        viewModel.onTicketAddedSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = it
                activity?.onBackPressed()
            }
        })
    }

    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.MESSAGE -> {
                showMessageDialog(inputError.message)
            }

        }
    }
}