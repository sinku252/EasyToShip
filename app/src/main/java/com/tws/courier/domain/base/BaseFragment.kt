package com.tws.courier.domain.base

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tws.courier.AppManager
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.data.RepoListener
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.dialogs.MessageDialog
import com.tws.courier.domain.dialogs.ProgressDialog
import com.tws.courier.domain.utils.Utils
import com.tws.courier.showMessageDialog
import com.tws.courier.ui.home.HomeActivity

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment(), RepoListener {

    lateinit var mParentActivity: HomeActivity

    private var toolbar: Toolbar? = null

    abstract fun getToolbarMenuHandler(): ToolbarMenuHandler?

    @LayoutRes
    abstract fun getLayoutResource(): Int

    abstract fun getViewModelClass(): Class<T>

    lateinit var viewModel: T
    lateinit var viewBinding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        return viewBinding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        viewModel.repoListener = this
        // Hardcoded viewmodel binding variable be careful because it's necessary to define in xml file

        viewBinding.setVariable(BR.viewmodel, viewModel)
        viewBinding.setLifecycleOwner(this)
        viewBinding.executePendingBindings()
        getToolbarMenuHandler()?.apply {
            setHasOptionsMenu(true)
            toolbar = view.findViewById(this.toolbarId)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            if (TextUtils.isEmpty(this.toolbarTitle))
                (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
            else {
                if (this.toolbarTitleId == 0) {
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                        true
                    )
                    (activity as AppCompatActivity).supportActionBar?.title = this.toolbarTitle
                } else {
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                        false
                    )
                    (toolbar?.findViewById(this.toolbarTitleId) as TextView).text =
                        this.toolbarTitle
                    (toolbar?.findViewById(this.toolbarTitleId) as TextView).isSelected = true
                }
            }
            if (this.hasBackButton()) {
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
//                setToolbarBackButtonIcon(R.drawable.back)
                toolbar?.setNavigationOnClickListener({ activity?.onBackPressed() })
            }
        }

        viewModel.showToast.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToast(message = it)
            }
        })
        viewModel.showInputError.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showInputError(inputError = it)
            }
        })
        viewModel.closeKeyBoard.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                closeKeyboard()
            }
        })
        viewModel.showProgressDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it) showProgressDialog() else dismissProgressDialog()
            }
        })
        viewModel.showMessageDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showMessageDialog = it
            }
        })
    }

    fun hideBackButton(){
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }

//    var menuCreated: Menu? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        getToolbarMenuHandler()?.apply {
            if (this.hasMenu() && this.menuResource == 0)
                throw RuntimeException("You haven't specified menu resource for the fragment toolbar!")
            if (this.hasMenu())
                inflater.inflate(getToolbarMenuHandler()?.menuResource!!, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
//        this.menuCreated = menu
    }

    fun overrideBackButtonClickListener(onClickListener: View.OnClickListener) {
        getToolbarMenuHandler()?.apply {
            if (this.hasBackButton()) toolbar?.setNavigationOnClickListener(onClickListener)
        }
    }

    fun setToolbarTitle() {
        getToolbarMenuHandler()?.apply {
            if (TextUtils.isEmpty(this.toolbarTitle))
                (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
            else {
                if (this.toolbarTitleId == 0) {
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                        true
                    )
                    (activity as AppCompatActivity).supportActionBar?.title = this.toolbarTitle
                } else {
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                        false
                    )
                    (toolbar?.findViewById(this.toolbarTitleId) as TextView).text =
                        this.toolbarTitle
                }
            }
        }
    }

    fun setToolbarTitle(title: String) {
        title.let {
            getToolbarMenuHandler()?.apply {
                if (TextUtils.isEmpty(it))
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                        false
                    )
                else {
                    if (this.toolbarTitleId == 0) {
                        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                            true
                        )
                        (activity as AppCompatActivity).supportActionBar?.title = it
                    } else {
                        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(
                            false
                        )
                        (toolbar?.findViewById(this.toolbarTitleId) as TextView).text =
                            it
                    }
                }
            }
        }
    }

//    fun setToolbarTitleClickListener(toolbarTitleClickListener: View.OnClickListener) {
//        (toolbar?.findViewById(R.id.toolbarTitle) as TextView).setOnClickListener(toolbarTitleClickListener)
//    }

    fun setToolbarBackButtonIcon(@DrawableRes icon: Int) {
        toolbar?.setNavigationIcon(icon)
    }

    fun hideNavigationButton() {
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(false)
    }

    fun closeKeyboard() {
        context?.let {
            Utils.closeKeyBoard(it, view)
        }
    }

    fun showToast(message: String) {
        if (context != null && !TextUtils.isEmpty(message))
            Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.BOTTOM, 0, 0);
            }.show()
    }

    open fun showInputError(inputError: InputError) {}

    // pass @ToolbarMenuHandler as null if there is no toolbar
    interface ToolbarMenuHandler {
        // Toolbar id necessary
        @get:IdRes
        val toolbarId: Int

        // Toolbar title id if custom toolbar title is there OR pass 0 if default title area is being used
        @get:IdRes
        val toolbarTitleId: Int

        // Toolbar menu resource if hasToolbar
        @get:MenuRes
        val menuResource: Int

        // Toolbar title
        val toolbarTitle: String

        // True if toolbar has a menu and also provide menu resource
        fun hasMenu(): Boolean

        // Will toolbar should show back button by default
        fun hasBackButton(): Boolean
    }

    override fun onDataRequestFailed(dataRequestType: Int, statusCode: Int, message: String) {
        when (statusCode) {
            // You can also show API messages
            AppManager.ServerErrorCodes.UPDATE_APP -> {
                showMessageDialog(getString(R.string.app_name),
                    getString(R.string.msg_update_app_force),
                    View.OnClickListener { Utils.openAppInPlaystore(context) })
            }
            AppManager.ServerErrorCodes.UPDATE_APP_OPTIONAL -> {
                showMessageDialog(getString(R.string.app_name),
                    getString(R.string.msg_update_app_optional),
                    View.OnClickListener {
                        if (!AppManager.isOptionalAppUpdateDialogShown)
                            Utils.openAppInPlaystore(context)
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
            else -> {
                showToast(message)
            }
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

    data class InputError(@InputErrorType val errorType: Int, val message: String)

    private var progressDialog: ProgressDialog? = null

    fun getProgressDialog(): ProgressDialog? {
        return progressDialog
    }

    fun showProgressDialog() {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(context)
        progressDialog!!.show()
    }

    fun showProgressDialog(message: String) {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(context, message)
        progressDialog!!.show()
    }

    fun showProgressDialog(@NonNull progressDialogListener: ProgressDialog.ProgressDialogListener) {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(context, progressDialogListener)
        progressDialog!!.show()
    }

    fun showProgressDialog(
        message: String,
        @NonNull progressDialogListener: ProgressDialog.ProgressDialogListener
    ) {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing()) return
        progressDialog = ProgressDialog(context, message, progressDialogListener)
        progressDialog!!.show()
    }

    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.getDialog().isShowing())
            progressDialog!!.getDialog().dismiss()
    }

    fun showMessageDialog(message: String) {
        message?.let {
            context?.let { context ->
                MessageDialog(context, it).show()
//                AlertDialog.Builder(context).setMessage(message)
//                    .setNeutralButton(R.string.ok, { dialogInterface: DialogInterface, i: Int ->
//                        dialogInterface.dismiss()
//                    }).create().show()
            }
        }
    }

    override fun onDestroyView() {
        closeKeyboard()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeActivity) {
            mParentActivity = context
        }
    }
}