package com.app.monrotv.ui.dashboard.banners

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.app.monrotv.ui.auth.register.BannerFragment
import com.tws.courier.domain.models.HomeSlider

class BannersFragmentAdapter(fa: FragmentActivity, val homeSlider: List<HomeSlider>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = homeSlider.size

    override fun createFragment(position: Int): Fragment =
        BannerFragment.newInstance(homeSlider[position])
}
