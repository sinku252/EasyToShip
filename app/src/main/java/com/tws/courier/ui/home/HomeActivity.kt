package com.tws.courier.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.app.monrotv.domain.models.VideoDownload
import com.google.gson.Gson
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.ActivityHomeBinding
import com.tws.courier.domain.base.BaseActivity
import com.tws.courier.domain.models.ForgotPasswordResponse
import com.tws.courier.domain.models.Help
import com.tws.courier.domain.models.OrderSuccess
import com.tws.courier.showToastShort
import com.tws.courier.ui.account_setting.AccountSettingFragment
import com.tws.courier.ui.address.AddressFragment
import com.tws.courier.ui.address.addressList.AddressList
import com.tws.courier.ui.booking_success.BookingSuccessful
import com.tws.courier.ui.change_password.ChangePasswordFragment
import com.tws.courier.ui.chats.ChatFragment
import com.tws.courier.ui.create_order.FragmentCreateOrder
import com.tws.courier.ui.create_order.MapsActivity
import com.tws.courier.ui.dashboard.DashboardFragment
import com.tws.courier.ui.dashboard_home.DashboardHomeFragment
import com.tws.courier.ui.help.HelpFragment
import com.tws.courier.ui.home.base.HomeBaseFragmentListener
import com.tws.courier.ui.insurance.InsuranceFragment
import com.tws.courier.ui.login.LoginFragment
import com.tws.courier.ui.notification.NotificationFragment
import com.tws.courier.ui.onboarding.OnboardingFragment
import com.tws.courier.ui.orders.OrdersFragment
import com.tws.courier.ui.otp.OtpFragment
import com.tws.courier.ui.password.ForgotPasswordFragment
import com.tws.courier.ui.payment.PaymentActivity
import com.tws.courier.ui.profile.ProfileFragment
import com.tws.courier.ui.signup.SignUpFragment
import com.tws.courier.ui.splash.SplashFragment
import com.tws.courier.ui.token.TokenFragment
import com.tws.courier.ui.token_response.TokenResponseFragment
import com.tws.courier.ui.wallet.WalletFragment


class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>(), HomeBaseFragmentListener,
    AppManager.LogoutListener, AppManager.VideoFileDownloaderCallbacks {

    private var isFirstTimeBackPressed = false

    override fun logout() {
        AppManager.setUser(null)
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        navigateToLoginFragment()
    }

    override fun onSubscriptionExpired() {
        navigateToSubscriptionsFragment()
    }

    override fun onResetPasswordDone() {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        navigateToLoginFragment()
    }

    // Pass null for default color of app status bar
    override fun setStatusBarColor(color: Int?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            color?.let {
                window?.statusBarColor = it
            } ?: kotlin.run {
                window?.statusBarColor = ContextCompat.getColor(this, R.color.purple_200)
            }
    }

    override fun navigateToMovieListFragment(genreId: Long, genreTitle: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToMovieListFragment(type: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToMediaDetailsFragment(address: com.tws.courier.domain.models.Address) {
        TODO("Not yet implemented")
    }

    override fun navigateToMovieDetailsFragment(movieId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToTvShowDetailsFragment(tvShowId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToVideoPlayActivity(videoQueue: Array<String>, currentPlayIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun navigateToWebVideoPlayActivity(videoUrl: String) {
        TODO("Not yet implemented")
    }

    override fun invalidateDashboardDrawerUserInfo() {
        TODO("Not yet implemented")
    }


    override fun onChangePasswordDone() {
        onBackPressed()
    }

    override fun navigateToSettingsFragment() {
        TODO("Not yet implemented")
    }

    override fun navigateToContactUsFragment() {
        TODO("Not yet implemented")
    }


    override fun getLayoutResource(): Int = R.layout.activity_home
    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigateToSplashFragment()
//            navigateToLoginFragment()
//            navigateToSubscriptionsFragment()
//            navigateToRegisterFragment()
//            navigateToFingerprintFragment()
        }

//        navigateToVideoPlayActivity(arrayOf(
////            "https://player.vimeo.com/video/76979871?loop=false&amp;byline=false&amp;portrait=false&amp;title=false&amp;speed=true&amp;transparent=0&amp;gesture=media",
////            "https://www.youtube.com/embed/bTqVqk7FSmY?origin=https://plyr.io&amp;iv_load_policy=3&amp;modestbranding=1&amp;playsinline=1&amp;showinfo=0&amp;rel=0&amp;enablejsapi=1",
////            "https://player.vimeo.com/video/46926279",
////            "https://www.youtube.com/watch?v=1FI155CBrbw"
//        ), 0)
        observeLiveData()
        AppManager.logoutListener = this

        if(!mPreference?.isTokenSave!!)
        {
            if(mPreference!!.isLogin!!)
            {
                mPreference?.fcmToken?.let { viewModel.sendFCM(it) }
                mPreference?.isTokenSave=true
            }
        }

    }

    private fun observeLiveData()
    {
        viewModel.onListReceived.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                // fragmentListener?.navigateToDashboardFragment()
                arrayList = it
                mPreference!!.storeArrayList("allVehicleType", ArrayList(it))
                Log.e("fffffff", "" + mPreference!!.getArrayList("allVehicleType"))
                // mPreference!!.allVehicleType = it.toString()
            }
        })

    }

    override fun navigateToLoginFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, LoginFragment.newInstance()).commit()
    }

    override fun navigateToRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, SignUpFragment.newInstance()).commit()
    }


    override fun navigateToSplashFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SplashFragment.newInstance()).commit()

        //OnboardingFragment
    }

    override fun navigateToOnBoardingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, OnboardingFragment.newInstance()).commit()

    }

    override fun navigateToForgotPasswordFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, ForgotPasswordFragment.newInstance()).commit()
    }

    override fun navigateToOtpFragment(forgotPasswordResponse: ForgotPasswordResponse) {

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, OtpFragment.newInstance(forgotPasswordResponse.userId,
                forgotPasswordResponse.otp.toString())).commit()
    }

    override fun navigateToChangePasswordFragment(userId: String, otp: String) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, ChangePasswordFragment.newInstance(userId, otp)).commit()
    }

    override fun navigateToDashboardFragment() {
       /* supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, DashboardFragment.newInstance()).commit()*/

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, FragmentCreateOrder.newInstance()).commit()
    }

    override fun navigateToDashboardHomeFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                DashboardHomeFragment.newInstance(),
                DashboardHomeFragment.TAG
            )
            .addToBackStack(DashboardHomeFragment.TAG)
            .commit()
    }

    override fun navigateToProfileFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                ProfileFragment.newInstance(),
                ProfileFragment.TAG
            )
            .addToBackStack(ProfileFragment.TAG)
            .commit()
    }

    override fun navigateToNotificationFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                NotificationFragment.newInstance(),
                NotificationFragment.TAG
            )
            .addToBackStack(NotificationFragment.TAG)
            .commit()
    }

    override fun navigateToBookingSuccessFragment(orderSuccess: OrderSuccess) {

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                BookingSuccessful.newInstance(orderSuccess),
                BookingSuccessful.TAG
            )
            .addToBackStack(BookingSuccessful.TAG)
            .commit()
    }

    override fun navigateToWallet() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                WalletFragment.newInstance(),
                WalletFragment.TAG
            )
            .addToBackStack(WalletFragment.TAG)
            .commit()
    }

    override fun navigateToInsurance() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                InsuranceFragment.newInstance(),
                InsuranceFragment.TAG
            )
            .addToBackStack(InsuranceFragment.TAG)
            .commit()
    }

    override fun navigateToAccountSetting() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                AccountSettingFragment.newInstance(),
                AccountSettingFragment.TAG
            )
            .addToBackStack(AccountSettingFragment.TAG)
            .commit()
    }

    override fun navigateToCreateShipment() {
       // startActivityForResult(Intent(this, MapsActivity::class.java), 200)
        //startActivity(Intent(this,MapsActivity::class.java))
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                FragmentCreateOrder.newInstance(),
                FragmentCreateOrder.TAG
            )
            .addToBackStack(FragmentCreateOrder.TAG)
            .commit()
        /*supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                CreateShipmentFragment.newInstance(),
                CreateShipmentFragment.TAG
            )
            .addToBackStack(CreateShipmentFragment.TAG)
            .commit()*/
    }

    override fun navigateToAddNewAddress() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                AddressFragment.newInstance(),
                AddressFragment.TAG
            )
            .addToBackStack(AddressFragment.TAG)
            .commit()
    }

    override fun navigateToListAddress(boolean: Boolean) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                AddressList.newInstance(boolean),
                AddressList.TAG
            )
            .addToBackStack(AddressList.TAG)
            .commit()
    }

    override fun navigateToOrdersFragment(status: Int) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                OrdersFragment.newInstance(status),
                OrdersFragment.TAG
            )
            .addToBackStack(OrdersFragment.TAG)
            .commit()
    }

    override fun navigateToPaymentActivity(amt: String) {
        startActivity(PaymentActivity.createIntentLauncher(this, amt))
    }

    override fun navigateToHelpFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                HelpFragment.newInstance(),
                HelpFragment.TAG
            )
            .addToBackStack(HelpFragment.TAG)
            .commit()
    }

    override fun navigateToTokenFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                TokenFragment.newInstance(),
                TokenFragment.TAG
            )
            .addToBackStack(TokenFragment.TAG)
            .commit()
    }

    override fun navigateToChatFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                ChatFragment.newInstance(),
                ChatFragment.TAG
            )
            .addToBackStack(ChatFragment.TAG)
            .commit()
    }

    override fun navigateToTokenResponseFragment(help:Help) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .add(
                R.id.container,
                TokenResponseFragment.newInstance(help),
                TokenResponseFragment.TAG
            )
            .addToBackStack(TokenResponseFragment.TAG)
            .commit()
    }

    override fun navigateToCreateOrder() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, FragmentCreateOrder.newInstance()).commit()
    }


    override fun navigateToVerifyOtpFragment(email: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToResetPasswordFragment(email: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToSubscriptionsFragment() {
        TODO("Not yet implemented")
    }

    override fun navigateToSubscriptionsFragment(showBack: Boolean) {
        TODO("Not yet implemented")
    }

    override fun navigateToSearchFragment() {
        TODO("Not yet implemented")
    }

    override fun navigateToPaymentFragment() {
        TODO("Not yet implemented")
    }

    override fun navigateToDownloadedVideosFragment() {
        TODO("Not yet implemented")
    }


    override fun onBackPressed() {
        /*fingerprintFragment?.let { fr ->
            if (fr.isVisible) performDoubleBackAction()
        }*/
    /*    val fragment: Fragment? = supportFragmentManager.findFragmentByTag("yourstringtag")
        if (fragment inst BookingSuccessful)
        {

        }*/

        val test: BookingSuccessful? =
            supportFragmentManager.findFragmentByTag("BookingSuccessful") as BookingSuccessful?
        if (test != null && test.isVisible()) {
            //DO STUFF
            navigateToDashboardFragment()
        } else {
            //Whatever
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
            return
        }
        performDoubleBackAction()
    }

    private fun performDoubleBackAction() {
        if (isFirstTimeBackPressed)
            super.onBackPressed()
        else {
            showToastShort = getString(R.string.msg_back_exit)
            isFirstTimeBackPressed = true
            Handler().postDelayed(Runnable { isFirstTimeBackPressed = false }, 1500)
        }
    }

    override fun hideFullScreen() {
        // Show the system bar
        viewBinding.constMain.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun showFullScreen() {
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        viewBinding.constMain.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onDownloadingStarted(videoDownload: VideoDownload) {
        TODO("Not yet implemented")
    }

    override fun onDownloadingCompleted(videoDownload: VideoDownload) {
        TODO("Not yet implemented")
    }

    override fun onDownloadingFailed(videoDownload: VideoDownload) {
        TODO("Not yet implemented")
    }

    override fun onDownloadingProgress(videoDownload: VideoDownload, progress: Int) {
        TODO("Not yet implemented")
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 200) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        if (it.hasExtra("OrderSuccess")) {
                            var gson = Gson()
                            var strObj = data.getStringExtra("OrderSuccess")
                            var orderSuccess: OrderSuccess = gson.fromJson(strObj, OrderSuccess::class.java)
                            navigateToBookingSuccessFragment(orderSuccess)
                        }
                       else
                        {
                            navigateToFragment(data.getStringExtra("type"))
                        }
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }*/

    fun navigateToFragment(type: String)
    {
        if(type.equals("home"))
        {
            navigateToDashboardFragment()
        }
        else if(type.equals("ticket"))
        {
            navigateToTokenFragment()
        }
        else if(type.equals("chat"))
        {
            navigateToChatFragment()
        }
        else if(type.equals("dashHome"))
        {
            navigateToDashboardHomeFragment()
        }
        else if(type.equals("profile"))
        {
            navigateToProfileFragment()
        }
        else if(type.equals("notification"))
        {
            navigateToNotificationFragment()
        }
        else
            showMessageDialog("Coming soon...")

        /*when (type) {
            type.equals("home") -> {

            }
            else -> showMessageDialog("Coming soon...")
        }*/

    }


}