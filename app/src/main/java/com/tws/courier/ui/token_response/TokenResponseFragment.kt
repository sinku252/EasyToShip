package com.tws.courier.ui.token_response

import android.os.Bundle
import android.view.View
import com.tws.courier.R
import com.tws.courier.databinding.FragmentTokenReponseBinding
import com.tws.courier.ui.home.base.HomeBaseFragment


class TokenResponseFragment : HomeBaseFragment<TokenResponseViewModel, FragmentTokenReponseBinding>()
{
    companion object {
        val TAG = "TokenResponseFragment"
        fun newInstance() = TokenResponseFragment()
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
    override fun getLayoutResource(): Int = R.layout.fragment_token_reponse
    override fun getViewModelClass(): Class<TokenResponseViewModel> = TokenResponseViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData()
    {

    }
}