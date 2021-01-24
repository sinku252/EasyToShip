package com.tws.courier.ui.home.base

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.databinding.ViewDataBinding
import com.tws.courier.domain.base.BaseFragment
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.utils.KotlinPreferencesHelper
import com.tws.courier.domain.utils.PreferenceHelper
import com.tws.courier.domain.utils.PreferenceHelper.defaultPrefs
import com.tws.courier.domain.utils.SharedPreferencesHelper
import org.koin.android.ext.android.get


abstract class HomeBaseFragment<T : BaseViewModel, B : ViewDataBinding> : BaseFragment<T, B>() {

    var fragmentListener: HomeBaseFragmentListener? = null
    var mPreference: KotlinPreferencesHelper? = null
    lateinit var PREFERENCE_KEY: String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeBaseFragmentListener)
        {
            fragmentListener = context
            mPreference = KotlinPreferencesHelper(context)
            //    prefs = defaultPrefs(context)
        }

        else
            throw RuntimeException("$context must implement HomeBaseFragmentListener")
    }

    override fun onDetach() {
        super.onDetach()
        fragmentListener = null
    }
}