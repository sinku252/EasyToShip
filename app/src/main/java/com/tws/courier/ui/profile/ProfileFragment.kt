package com.tws.courier.ui.profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.FragmentProfileBinding
import com.tws.courier.domain.models.User
import com.tws.courier.showMessageDialog

import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.home.base.HomeBaseMediaOptionsFragment
import java.io.File

class ProfileFragment : HomeBaseMediaOptionsFragment<ProfileViewModel, FragmentProfileBinding>()
{
    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = R.menu.menu_profile
        override val toolbarTitle: String
            get() = getString(R.string.profile)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_profile
    override fun getViewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        initViews()
    }

    fun observeLiveData()
    {

        viewBinding.user=AppManager.getUser()

        viewModel.onUpdateProfileImage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                val user = AppManager.getUser()!!
                user.imagePath = it.imagePath
                AppManager.setUser(user)
                fragmentListener?.invalidateDashboardDrawerUserInfo()
                //user.imagePath?.let { setUserImageOrNothing(it) }
                setProfileDetails(it)
            }
        })

        viewModel.onlogoutRecived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showMessageDialog(getString(R.string.app_name), getString(R.string.warn_logout),
                    View.OnClickListener { mPreference?.isLogin = false
                                            fragmentListener?.logout() },
                    View.OnClickListener { })

            }
        })



    }

    fun setProfileDetails(user: User) {
        /*viewBinding.editUsername.setTextOrEmpty(user.name)
        viewBinding.editEmail.setTextOrEmpty(user.email)
        viewBinding.editPhone.setTextOrEmpty(user.mobile)

        viewBinding.textPlanExpireInfo.setTextOrEmpty("Expires on " + user.next_billing_date)
        if (user.cancelled) {
            // viewBinding.textPlanExpireInfo.visibility=View.GONE
            viewBinding.buttonCancelPlan.text = getString(R.string.resume_plan)
        } else {
            // viewBinding.textPlanExpireInfo.visibility=View.VISIBLE
            viewBinding.buttonCancelPlan.text = getString(R.string.cancel_plan)
        }*/
        user.imagePath?.let { setUserImageOrNothing(it) }
        /*if (user.isAdmin == 0)
            user.planInfo?.let { setPlanInfo(it) }
        else if (user.isAdmin == 1)
            viewBinding.constPlan.visibility = View.GONE*/
    }
    fun setUserImageOrNothing(imageUrl: String) {
        if (URLUtil.isValidUrl(imageUrl))
            context?.let { Glide.with(it).load(imageUrl).centerCrop().into(viewBinding.userProfile) }
    }

    private fun initViews() {

        viewBinding.profileImageUpload.setOnClickListener {
            context?.let { context ->
                AlertDialog.Builder(context)?.apply {
                    setItems(
                        arrayOf("Select Photo From Gallery", "Capture Photo"),
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                0 -> pickPhotoFromGallery()
                                1 -> capturePhoto()
                            }
                            dialog.dismiss()
                        })
                }.create().show()
            }
        }
    }

    companion object {
        val TAG = "FragmentProfile"
        fun newInstance() = ProfileFragment()
    }

    override fun onCameraPermissionsDenied() {
        showMessageDialog( "Required permissions were denied!")
    }

    override fun onPhotoReceived(path: String) {
        viewModel.selectedFilePath = path
        activity?.runOnUiThread {
            context?.let {
                Glide.with(it).load(File(path)).into(viewBinding.userProfile)
                viewModel.requestUpdateUserProfile()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
//                showMessageDialog("Coming soon...")
                fragmentListener?.navigateToAccountSetting()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}