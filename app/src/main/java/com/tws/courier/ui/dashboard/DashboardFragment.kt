package com.tws.courier.ui.dashboard

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.monrotv.ui.dashboard.adapters.DrawerAdapter
import com.app.monrotv.ui.dashboard.banners.BannersFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tws.courier.R
import com.tws.courier.databinding.FragmentDashboardBinding
import com.tws.courier.domain.base.BaseDrawerFragment
import com.tws.courier.domain.models.HomeSlider
import com.tws.courier.domain.utils.Utils.shareApp
import com.tws.courier.showMessageDialog
import com.tws.courier.ui.create_order.MapsActivity
import com.tws.courier.ui.dashboard.adapters.ImageSliderAdapter
import com.tws.courier.ui.home.base.HomeBaseDrawerFragment


class DashboardFragment : HomeBaseDrawerFragment<DashboardViewModel, FragmentDashboardBinding>()
     {



    var images = intArrayOf(
        R.drawable.lock, R.drawable.lock
    )

    companion object {
        val TAG = "DashboardFragment"
        fun newInstance() = DashboardFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = R.menu.menu_dashboard
        override val toolbarTitle: String
            get() = getString(R.string.home)

        override fun hasMenu(): Boolean = true
        override fun hasBackButton(): Boolean = true
    }

    override fun getLayoutResource(): Int = R.layout.fragment_dashboard
    override fun getViewModelClass(): Class<DashboardViewModel> = DashboardViewModel::class.java

    val drawerAdapter: DrawerAdapter = DrawerAdapter(
        DrawerAdapter.DrawerItem.getItemsList,
        object : DrawerAdapter.DrawerAdapterCallbacks {
            override fun onDrawerItemClicked(drawerItem: DrawerAdapter.DrawerItem) {
                closeDrawer()
                when (drawerItem.id) {
                    33 -> { // nothing yet
                        //viewModel.requestDashboardData()
                        //  fragmentListener?.navigateToDashboardFragment()
                    }
                    34 -> {
                        fragmentListener?.navigateToCreateShipment()
                    }
                    35 -> {
                        fragmentListener?.navigateToDashboardHomeFragment()
                    }
                    36 -> {
                        fragmentListener?.navigateToAccountSetting()
                    }
                    37 -> {
                        fragmentListener?.navigateToWallet()
                    }
                    38 -> {
                        fragmentListener?.navigateToProfileFragment()
                    }
                    39 -> {
                        fragmentListener?.navigateToChatFragment()
                    }
                    40 -> {
                        //shareApp(activity)
                       fragmentListener?.navigateToTokenFragment()
                       // fragmentListener?.navigateToNotificationFragment()
                    }
                    41 -> {
                        shareApp(activity)
                    }
                    42 -> {
                        fragmentListener?.navigateToNotificationFragment()
                    }
                    43 -> {
                        //contact us
                    }
                    44 -> {
                        showMessageDialog(getString(R.string.app_name), getString(R.string.warn_logout),
                            View.OnClickListener { mPreference?.isLogin = false
                                fragmentListener?.logout() },
                            View.OnClickListener { })
                    }


                    else -> showMessageDialog("Coming soon...")
                }
            }

            override fun onHeaderClicked() {
//                closeDrawer()
//                showMessageDialog("Coming soon...")
            }
        })

    lateinit var sliderList:ArrayList<HomeSlider>

    var adapter = activity?.let { ImageSliderAdapter(it, sliderList) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDrawer()
        //viewBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setBanners()
        observeLiveData()
        viewModel.getSlider()
    }

    private fun observeLiveData() {
        viewModel.navigateToDashboard.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToDashboardHomeFragment()
            }
        })

        viewModel.navigateToAccountSetting.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToAccountSetting()
            }
        })

        viewModel.navigateToHelp.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToHelpFragment()
            }
        })

        viewModel.navigateToWallet.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToWallet()
            }
        })

        viewModel.navigateToCreateShipment.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToCreateShipment()
            }
        })

        viewModel.onsliderListReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {

                // val listAlt: ArrayList<HomeSlider> = ArrayList()
                if (it.size > 0) {
                    activity?.let { act ->
                        val l = ArrayList(it)
                        it.forEach { l.add(it) }
                        val pagerAdapter = BannersFragmentAdapter(act, it)
                        viewBinding.pagerBanners.adapter = pagerAdapter

                        TabLayoutMediator(viewBinding.intoTabLayout, viewBinding.pagerBanners)
                        { tab, position -> }.attach()
                    }
                }
            }
        })

    }

    private fun setAddressToviews(list: List<HomeSlider>) {
        if (list.isNotEmpty()) {
            adapter?.addItems(list)
        }
    }

    private fun setBanners() {

     //   viewBinding.pagerBanners.adapter = adapter
       /* images.let { list ->
            if (list.size > 0) {
                activity?.let { act ->
                    val l = ArrayList(list)
                    list.forEach { l.add(it) }
                    val pagerAdapter = BannersFragmentAdapter(act, list)
                    viewBinding.pagerBanners.adapter = pagerAdapter

                    *//*TabLayoutMediator(viewBinding.intoTabLayout, viewBinding.pagerBanners)
                    { tab, position -> }.attach()*//*
                }

            } else viewBinding.layoutBanners.visibility = View.GONE
        }*/
    }


    private fun setupDrawer() {
        // Calling the magical method will do the rest
        initDrawer(object : BaseDrawerFragment.DrawerCreationInfo {
            override val drawerRecyclerView: RecyclerView = viewBinding.recyclerDrawer
            override var drawerRecyclerLayoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(context)
            override val drawerRecyclerAdapter: RecyclerView.Adapter<*> = drawerAdapter
            override val drawerGravity: Int = GravityCompat.START
            override val drawerOpenContentDesc: Int = R.string.drawer_close
            override val drawerCloseContentDesc: Int = R.string.drawer_close
            override val drawerLayout: DrawerLayout = viewBinding.drawerLayout
        })

        setToolbarBackButtonIcon(R.drawable.nav)
        overrideBackButtonClickListener(View.OnClickListener {
            openCloseDrawer()
        })
    }

    private fun setupRecyclerDrawableDividerDecoration(
        recyclerView: RecyclerView,
        @DrawableRes drawableRes: Int
    ) {
        context?.let { context ->
            ContextCompat.getDrawable(context, drawableRes)
                ?.let { drawable ->
                    recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.HORIZONTAL
                        ).apply {
                            setDrawable(drawable)
                        })
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
//                showMessageDialog("Coming soon...")
                fragmentListener?.navigateToProfileFragment()
                true
            }
            R.id.action_notification -> {
//                showMessageDialog("Coming soon...")
                fragmentListener?.navigateToNotificationFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*override fun openDrawerLayout() {
        openCloseDrawer()
    }*/
}