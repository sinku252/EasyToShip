package com.tws.courier.ui.token_response

import android.os.Bundle
import android.view.View
import com.tws.courier.R
import com.tws.courier.databinding.FragmentTokenReponseBinding
import com.tws.courier.domain.models.Help
import com.tws.courier.domain.models.OrderSuccess
import com.tws.courier.ui.booking_success.BookingSuccessful
import com.tws.courier.ui.home.base.HomeBaseFragment


class TokenResponseFragment : HomeBaseFragment<TokenResponseViewModel, FragmentTokenReponseBinding>()
{
    companion object {
        val TAG = "TokenResponseFragment"
        fun newInstance() = TokenResponseFragment()

        val ARG_HELP = "help"

        fun newInstance(help: Help) = TokenResponseFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_HELP, help)
            }
        }
    }

    private var help: Help? = null


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
    override fun getLayoutResource(): Int = R.layout.fragment_token_reponse
    override fun getViewModelClass(): Class<TokenResponseViewModel> = TokenResponseViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_HELP)) help = it.getParcelable(ARG_HELP)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData()
    {
        viewBinding.help=help


    }
}