package com.tws.courier.domain.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.tws.courier.R
import com.tws.courier.databinding.FragmentAcountSettingBinding
import com.tws.courier.setTextOrEmpty
import com.tws.courier.ui.account_setting.AccountSettingViewModel

class CustomDialogClass:  BaseDialogFragment<AccountSettingViewModel, FragmentAcountSettingBinding>() {

    override fun getViewModelClass(): Class<AccountSettingViewModel> = AccountSettingViewModel::class.java
    override val layoutResource: Int
        get() = R.layout.dialog_add_money

    /* init {
         setCancelable(false)
     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         requestWindowFeature(Window.FEATURE_NO_TITLE)
         setContentView(R.layout.dialog_add_money)

     }*/
}