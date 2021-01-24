package com.tws.courier.ui.insurance

import android.os.Bundle
import android.view.View
import com.tws.courier.R
import com.tws.courier.databinding.FragmentDashboardHomeBinding
import com.tws.courier.databinding.FragmentInsuranceBinding
import com.tws.courier.ui.dashboard_home.DashboardHomeFragment
import com.tws.courier.ui.dashboard_home.DashboardHomeViewModel
import com.tws.courier.ui.home.base.HomeBaseFragment

class InsuranceFragment : HomeBaseFragment<InsuranceViewModel, FragmentInsuranceBinding>()
{


    companion object {
        val TAG = "InsuranceFragment"
        fun newInstance() = InsuranceFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.insurance)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_insurance
    override fun getViewModelClass(): Class<InsuranceViewModel> = InsuranceViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}