package com.tws.courier.domain.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tws.courier.BR
import com.tws.courier.data.RepoListener
import com.tws.courier.domain.base.BaseFragment
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.utils.Utils

abstract class BaseDialogFragment<T : BaseViewModel, B : ViewDataBinding> : DialogFragment(),
    RepoListener {

    lateinit var viewModel: T
    lateinit var viewBinding: B

    @get:LayoutRes
    protected abstract val layoutResource: Int

    abstract fun getViewModelClass(): Class<T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutResource, container, false)
        return viewBinding.getRoot()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            //            dialog.getWindow().setGravity(Gravity.BOTTOM);
//            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.window?.requestFeature(Window.FEATURE_NO_TITLE)
//            this.window?.attributes.windowAnimations = R.style.DialogAnimationOne

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        viewModel.repoListener = this
        // Hardcoded viewmodel binding variable be careful because it's necessary to define in xml file
        viewBinding.setVariable(BR.viewmodel, viewModel)
        viewBinding.setLifecycleOwner(this)
        viewBinding.executePendingBindings()

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

    open fun showInputError(inputError: BaseFragment.InputError) {}

    override fun onDataRequestFailed(dataRequestType: Int, statusCode: Int, message: String) {
        showToast(message)
    }

    override fun onNetworkConnectionError(dataRequestType: Int, message: String) {
        showToast(message)
    }

    override fun setDataRequestProgressIndicator(dataRequestType: Int, visible: Boolean) {
        if (visible) showProgressDialog()
        else dismissProgressDialog()
    }

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
}