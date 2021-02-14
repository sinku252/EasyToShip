package com.tws.courier.ui.home.base

import com.tws.courier.domain.models.ForgotPasswordResponse
import com.tws.courier.domain.models.Help
import com.tws.courier.domain.models.OrderSuccess


interface HomeBaseFragmentListener {

    fun navigateToLoginFragment()
    fun navigateToRegisterFragment()
    fun navigateToSplashFragment()
    fun navigateToOnBoardingFragment()
    fun navigateToForgotPasswordFragment()
    fun navigateToOtpFragment(forgotPasswordResponse: ForgotPasswordResponse)
    fun navigateToChangePasswordFragment(userId:String,otp:String)
    fun navigateToDashboardFragment()
    fun navigateToDashboardHomeFragment()
    fun navigateToProfileFragment()
    fun navigateToAccountSetting()
    fun navigateToCreateShipment()
    fun navigateToAddNewAddress()
    fun navigateToListAddress(boolean: Boolean)
    fun navigateToOrdersFragment(param:Int)
    fun navigateToNotificationFragment()
    fun navigateToBookingSuccessFragment(orderSuccess: OrderSuccess)
    fun navigateToWallet()
    fun navigateToPaymentActivity(amt:String)
    fun navigateToHelpFragment()
    fun navigateToTokenFragment()
    fun navigateToChatFragment()
    fun navigateToTokenResponseFragment(help:Help)
    fun navigateToCreateOrder()
    fun navigateToInsurance()

//    fun navigateToResetPasswordFragment()

    fun navigateToVerifyOtpFragment(email: String)
    fun navigateToResetPasswordFragment(email: String)
    fun navigateToSubscriptionsFragment()
    fun navigateToSubscriptionsFragment(showBack: Boolean)
    fun navigateToSearchFragment()
   // fun navigateToPaymentFragment(packagePlan: PackagePlan)
   fun navigateToPaymentFragment()
    fun navigateToDownloadedVideosFragment()


    fun showFullScreen()
    fun hideFullScreen()

    fun logout()
    fun onResetPasswordDone()

    fun setStatusBarColor(color: Int?) // pass null for default color of app
    fun navigateToMovieListFragment(genreId: Long, genreTitle: String)
    fun navigateToMovieListFragment(type: String)

    //fun navigateToMediaDetailsFragment(movie: Movie)
    fun navigateToMediaDetailsFragment(address: com.tws.courier.domain.models.Address)
    fun navigateToMovieDetailsFragment(movieId: Long)
    fun navigateToTvShowDetailsFragment(tvShowId: Long)

    fun navigateToVideoPlayActivity(videoQueue: Array<String>, currentPlayIndex: Int)
    fun navigateToWebVideoPlayActivity(videoUrl: String)

    fun invalidateDashboardDrawerUserInfo()
    fun onChangePasswordDone()
    fun navigateToSettingsFragment()
    fun navigateToContactUsFragment()

}