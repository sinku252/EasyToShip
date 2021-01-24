package com.tws.courier.domain.utils

import android.content.SharedPreferences

class SharedPreferencesHelper
internal constructor(val mPrefs: SharedPreferences) {


    var welcome: Boolean
        get() = mPrefs.getBoolean("welcome", false)
        set(value) = mPrefs.edit().putBoolean("welcome", value).apply()

    var isLogin: Boolean
        get() = mPrefs.getBoolean("isLogin", false)
        set(value) = mPrefs.edit().putBoolean("isLogin", value).apply()

}