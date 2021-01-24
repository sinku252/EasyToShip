package com.tws.courier.ui.dashboard_home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.tws.courier.R
import com.tws.courier.databinding.FragmentDashboardHomeBinding

import com.tws.courier.ui.home.base.HomeBaseFragment


class DashboardHomeFragment : HomeBaseFragment<DashboardHomeViewModel, FragmentDashboardHomeBinding>()
{
    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.dashboard)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_dashboard_home
    override fun getViewModelClass(): Class<DashboardHomeViewModel> = DashboardHomeViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestDashdata()
        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.onGetDashDataReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                viewBinding.dashList=it
                //Log.e("asffsaf",""+it)
             /*   it.forEach { mo ->
                    DashBoard(mo)
                }*/
           //     viewBinding?.viewmodel?.newOrder=it.get(0)
                /*viewBinding?.viewmodel?.newOrder=it.get(0)
                viewBinding?.viewmodel?.pickup=it.get(1)
                viewBinding?.viewmodel?.undelivered=it.get(2)
                viewBinding?.viewmodel?.outForDelivery=it.get(3)
                viewBinding?.viewmodel?.delivered=it.get(4)*/
                /*val listAlt: ArrayList<String> = ArrayList()
                val list = ArrayList(it)
                list.forEach { mo ->
                    listAlt.add(mo)
                }
                list.clear()
                list.addAll(listAlt)
                setDataToviews(list)*/

            }
           // viewModel.repoListener
        })
        viewModel.navigateToOrders.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToOrdersFragment(it)
            }
        })
    }

    fun setDataToviews(list:ArrayList<String>)
    {
       viewBinding?.viewmodel?.newOrder=list.get(0)
    }


    companion object {
        val TAG = "DashboardHomeFragment"
        fun newInstance() = DashboardHomeFragment()
    }
}