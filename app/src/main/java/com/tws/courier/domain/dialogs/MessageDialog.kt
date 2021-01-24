package com.tws.courier.domain.dialogs

import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.tws.courier.R
import com.tws.courier.setTextOrEmpty

class MessageDialog(val aContext: Context, val message: String) : BaseDialog() {

    override fun getDialogStyle(): Int = R.style.AppTheme_NoBackDialog
    override fun getContext(): Context = aContext
    override fun getLayoutResource(): Int = R.layout.dialog_message

    init {
        initDialog()
        initViews()
    }

    protected fun initViews() {
        if (view == null) return
        view.findViewById<TextView>(R.id.text_message).setTextOrEmpty(message)
        view.findViewById<Button>(R.id.button_ok).setOnClickListener {
            dialog.dismiss()
        }
    }

    public override fun show() {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setDimAmount(0.7f)
        dialog.show()
    }
}