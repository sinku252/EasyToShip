package com.tws.courier.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tws.courier.R
import com.tws.courier.databinding.FragmentOnboardingBinding
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_onboarding.*


class OnboardingFragment : HomeBaseFragment<OnboardingViewModel, FragmentOnboardingBinding>()
{
    companion object {
        fun newInstance() = OnboardingFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_onboarding
    override fun getViewModelClass(): Class<OnboardingViewModel> = OnboardingViewModel::class.java

    var currentTab = 0
    var tabCount = 0
    private var layouts= ArrayList<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeLiveData()
    }

    fun init() {
        layouts = arrayListOf(
            R.layout.item_welcome_lay1,
            R.layout.item_welcome_lay2,
            R.layout.item_welcome_lay3,
            R.layout.item_welcome_lay4
        )

        tabCount = layouts.size
        //adapter1 = ViewPagerAdapter(activity, layouts)
        viewPager.adapter = activity?.let { ViewPagerAdapter(it, layouts) }


       /* val pageTransformer = ParallaxTransformer()
        viewPager.setPageTransformer(true, pageTransformer)*/


        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

        })
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                currentTab = position + 1
                if (currentTab == tabCount) {
                    skip.visibility=View.VISIBLE
                   // skip.text = getString(R.string.continues)
                } else {
                    skip.visibility=View.GONE
                    //skip.text = getString(R.string.skip)
                }
            }

            override fun onPageSelected(position: Int) {
            }

        })


        /*next.setOnClickListener(this)
        skip.setOnClickListener(this)*/
    }

    private fun observeLiveData() {

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                fragmentListener?.navigateToLoginFragment()
            }
        })
    }

}

