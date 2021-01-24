package com.tws.courier.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.FragmentSplashBinding
import com.tws.courier.imageLocalNoPlaceholders

class SplashFragment : HomeBaseFragment<SplashViewModel, FragmentSplashBinding>() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_splash
    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        fragmentListener?.showFullScreen()
      //  viewBinding.image.imageLocalNoPlaceholders = R.drawable.ef_image_placeholder

        Handler().postDelayed({
            if (!mPreference?.welcome!!) {
                mPreference!!.welcome = true
                fragmentListener?.navigateToOnBoardingFragment()
            }
            else{
                if (mPreference!!.isLogin)
                    fragmentListener?.navigateToDashboardFragment()
                else fragmentListener?.navigateToLoginFragment()
                    //fragmentListener?.navigateToCreateOrder()
            }

        }, 3000)

//        Handler().postDelayed({
//          fragmentListener?.hideFullScreen()
//        }, 2000)
    }

}