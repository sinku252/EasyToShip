package com.tws.courier.domain.dialogs

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.tws.courier.R
import com.tws.courier.setTextOrEmpty

class ListDialog(
    val aContext: Context,
    val title: String,
    val message: String,
    var neutralClickListener: View.OnClickListener? = null,
    var positiveClickListener: View.OnClickListener? = null,
    var negativeClickListener: View.OnClickListener? = null
) :
    BaseDialog() {

    override fun getDialogStyle(): Int = R.style.AppTheme_NoBackDialog
    override fun getContext(): Context = aContext
    override fun getLayoutResource(): Int = R.layout.dialog_list

    init {
        initDialog()
        initViews()
    }

    protected fun initViews() {
        if (view == null) return
        view.findViewById<TextView>(R.id.text_title).setTextOrEmpty(title)
        view.findViewById<TextView>(R.id.text_message).setTextOrEmpty(message)

        neutralClickListener?.let {
            view.findViewById<Button>(R.id.button_ok)?.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    dialog.dismiss()
                    neutralClickListener?.onClick(null)
                }
            }
        }
        positiveClickListener?.let {
            view.findViewById<Button>(R.id.button_positive)?.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    dialog.dismiss()
                    positiveClickListener?.onClick(null)
                }
            }

        }
        negativeClickListener?.let {
            view.findViewById<Button>(R.id.button_negative)?.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    dialog.dismiss()
                    negativeClickListener?.onClick(null)
                }
            }
        }
        if (negativeClickListener != null && positiveClickListener != null && neutralClickListener == null)
            view.findViewById<Button>(R.id.button_ok).visibility = View.GONE
    }

    public override fun show() {
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setDimAmount(0.7f)
        dialog.show()
    }

//    private inner class ListAdapter : RecyclerView.Adapter<>{
//
//    }
}