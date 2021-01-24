package com.tws.courier.domain.base

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Slide.GravityFlag
import com.tws.courier.AppManager
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.data.RepoListener
import com.tws.courier.domain.dialogs.MessageDialog
import com.tws.courier.domain.dialogs.ProgressDialog
import com.tws.courier.domain.models.OrderCalculation
import com.tws.courier.domain.utils.KotlinPreferencesHelper
import com.tws.courier.domain.utils.Utils
import com.tws.courier.showMessageDialog

abstract class BaseDrawerKotlin <T : BaseViewModel, B : ViewDataBinding> : AppCompatActivity(),
    RepoListener {
    //                    animateImageUser(slideOffset);
    lateinit var viewModel: T
    lateinit var viewBinding: B
    abstract fun getViewModelClass(): Class<T>
    lateinit var arrayList: List<OrderCalculation>
    var mPreference: KotlinPreferencesHelper? = null
    @LayoutRes
    abstract fun getLayoutResource(): Int

    private var drawerToggle: ActionBarDrawerToggle? = null
    lateinit var drawerCreationInfo: BaseDrawerFragment.DrawerCreationInfo
//    abstract fun getViewModelBindingId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        viewBinding = DataBindingUtil.setContentView(this, getLayoutResource())
        viewModel.repoListener = this
        mPreference = KotlinPreferencesHelper(this)
        // Hardcoded viewmodel binding variable be careful because it's necessary to define in xml file
        viewBinding.setVariable(BR.viewmodel, viewModel)
//        viewBinding.setVariable(getViewModelBindingId(), viewModel)
        viewBinding.setLifecycleOwner(this)
        viewBinding.executePendingBindings()

        viewModel.showToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                showToast(message = it)
            }
        })
        viewModel.showInputError.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                showInputError(inputError = it)
            }
        })
        viewModel.closeKeyBoard.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                closeKeyboard()
            }
        })
    }

    fun showToast(message: String) {
        if (!TextUtils.isEmpty(message)) Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 0);
        }.show()
    }

//    fun showSnackBar(message: String) {
//        val rootView: View = getWindow().getDecorView().findViewById(android.R.id.content)
//            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
//    }

    fun closeKeyboard() {
        Utils.closeKeyBoard(this)
    }


    fun showMessageDialog(message: String) {
        message?.let {
            MessageDialog(this, it).show()
        }
    }


    override fun onDataRequestFailed(dataRequestType: Int, statusCode: Int, message: String) {
        when (statusCode) {
            // You can also show API messages
            AppManager.ServerErrorCodes.UPDATE_APP -> {
                showMessageDialog(getString(R.string.app_name),
                    getString(R.string.msg_update_app_force),
                    View.OnClickListener { Utils.openAppInPlaystore(this) })
            }
            AppManager.ServerErrorCodes.UPDATE_APP_OPTIONAL -> {
                showMessageDialog(getString(R.string.app_name),
                    getString(R.string.msg_update_app_optional),
                    View.OnClickListener {
                        if (!AppManager.isOptionalAppUpdateDialogShown)
                            Utils.openAppInPlaystore(this)
                        AppManager.isOptionalAppUpdateDialogShown = true
                    },
                    View.OnClickListener { })
            }
            AppManager.ServerErrorCodes.MAINTENANCE_TOKEN -> {
                showMessageDialog(getString(R.string.app_name), message,
                    View.OnClickListener { })
            }
            AppManager.ServerErrorCodes.USER_TOKEN_EXPIRED -> {
                AppManager.logoutListener?.logout()
            }
            AppManager.ServerErrorCodes.SUBSCRIPTION_EXPIRED -> {
                AppManager.logoutListener?.onSubscriptionExpired()
            }
            else -> showToast(message)
        }
    }

    override fun onNetworkConnectionError(dataRequestType: Int, message: String) {
        showToast(message)
    }

    override fun setDataRequestProgressIndicator(dataRequestType: Int, visible: Boolean) {
//        showToast("setDataRequestProgressIndicator : " + visible)
        if (visible) showProgressDialog()
        else dismissProgressDialog()
    }

    open fun showInputError(inputError: BaseFragment.InputError) {}

    private var progressDialog: ProgressDialog? = null

    fun getProgressDialog(): ProgressDialog? {
        return progressDialog
    }

    fun showProgressDialog() {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(this)
        progressDialog!!.show()
    }

    fun showProgressDialog(message: String) {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(this, message)
        progressDialog!!.show()
    }

    fun showProgressDialog(@NonNull progressDialogListener: ProgressDialog.ProgressDialogListener) {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(this, progressDialogListener)
        progressDialog!!.show()
    }

    fun showProgressDialog(
        message: String,
        @NonNull progressDialogListener: ProgressDialog.ProgressDialogListener
    ) {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(this, message, progressDialogListener)
        progressDialog!!.show()
    }

    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing())
            progressDialog!!.getDialog().dismiss()
    }



    fun initDrawer(drawerCreationInfo: BaseDrawerFragment.DrawerCreationInfo) {
        this.drawerCreationInfo = drawerCreationInfo
        drawerCreationInfo.let { info ->
            getDrawerToggle()?.let {
                info.drawerLayout.addDrawerListener(it)
                it.isDrawerIndicatorEnabled = true
            }
            info.drawerRecyclerView.layoutManager = info.drawerRecyclerLayoutManager
            info.drawerRecyclerView.adapter = info.drawerRecyclerAdapter
        }
        getDrawerToggle()?.syncState()
    }

    private fun getDrawerToggle(): ActionBarDrawerToggle? {
        if (drawerToggle == null)
            drawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerCreationInfo.drawerLayout,
                drawerCreationInfo.drawerOpenContentDesc,
                drawerCreationInfo.drawerCloseContentDesc
            ) {
//                override fun onDrawerClosed(view: View?) {
//                    super.onDrawerClosed(view)
//                }
//
//                override fun onDrawerOpened(drawerView: View?) {
//                    super.onDrawerOpened(drawerView)
//                }
//
//                override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
//                    super.onDrawerSlide(drawerView, slideOffset)
//                    //                    animateImageUser(slideOffset);
//                }
            }
        return drawerToggle
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerCreationInfo.let {
            getDrawerToggle()?.onConfigurationChanged(newConfig)
        }
    }

    fun closeDrawer() {
        if (drawerCreationInfo.drawerLayout.isDrawerOpen(
                drawerCreationInfo.drawerGravity
            )
        )
            drawerCreationInfo.drawerLayout.closeDrawer(drawerCreationInfo.drawerGravity)
    }

    fun openDrawer() {
        drawerCreationInfo.drawerLayout.openDrawer(drawerCreationInfo.drawerGravity)
    }

    fun openCloseDrawer() {
        if (drawerCreationInfo.drawerLayout.isDrawerOpen(drawerCreationInfo.drawerGravity))
            closeDrawer()
        else
            openDrawer()
    }



}