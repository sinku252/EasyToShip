package com.tws.courier.domain.utils

import android.util.Log
import com.tws.courier.AppManager

class Logger {

    companion object {

        fun d(s: String) {
            if (AppManager.IS_LOGGING_ENABLED) Log.d("", s)
        }

        fun d(tag: String, s: String) {
            if (AppManager.IS_LOGGING_ENABLED) Log.d(tag, s)
        }

        fun e(s: String) {
            if (AppManager.IS_LOGGING_ENABLED) Log.e("", s)
        }

        fun e(tag: String, s: String) {
            if (AppManager.IS_LOGGING_ENABLED) Log.e(tag, s)
        }

//        fun print(s: String) {
//            if (AppManager.IS_LOGGING_ENABLED) println(s)
//        }

        @JvmStatic
        fun print(s: String) {
            if (AppManager.IS_LOGGING_ENABLED) println(s)
        }
    }
}