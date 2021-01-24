package com.tws.courier.ui.home.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.tws.courier.domain.base.BaseDrawerFragment
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.utils.KotlinPreferencesHelper


abstract class HomeBaseDrawerFragment<T : BaseViewModel, B : ViewDataBinding> : BaseDrawerFragment<T, B>() {

    var fragmentListener: HomeBaseFragmentListener? = null
    var mPreference: KotlinPreferencesHelper? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeBaseFragmentListener)
        {
            fragmentListener = context
            mPreference = KotlinPreferencesHelper(context)
        }
        else
            throw RuntimeException("$context must implement HomeBaseFragmentListener")
    }

    override fun onDetach() {
        super.onDetach()
        fragmentListener = null
    }
}