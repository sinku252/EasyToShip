package com.tws.courier

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.room.Room
import com.app.monrotv.domain.models.VideoDownload
import com.tws.courier.data.local.AppDatabase
import com.tws.courier.domain.models.User
import com.tws.courier.domain.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class AppManager(context: Context) {

    init {
        if (instance == null) {
            preferences = context.applicationContext.getSharedPreferences(
                PrefKeys.PREF_NAME,
                Context.MODE_PRIVATE
            )
            appDatabase = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
        c = context
    }

    private interface PrefKeys {
        companion object {
            val PREF_NAME = "iPayPrefs"
            val PREF_DEVICE_ID = "dev_ice_iD"
            val PREF_USER = "Er_us:)"

            val PREF_SAVED_EMAIL = "emfvey"
            val PREF_SAVED_PASSWORD = "56ff"
            val PREF_FOOTER_PARTS = "81415"
            val PREF_IS_APP_SECURED = "237645"
        }
    }

    companion object {

        val IS_LOGGING_ENABLED = true

        // Demo Local
//        val REMOTE_DATA_BASE_URL = "https://monrotv.24livehost.com/api/v2/"
        // Live
        val REMOTE_DATA_BASE_URL = "https://developmentconsole.tech/courier/api/ApiController/init/"
        val REMOTE_DATA_UPLOAD_URL = "https://developmentconsole.tech/courier/api/ApiController/"

      //  http://developmentconsole.tech/courier/api/ApiController/upload_community_images

        val API_VERSION = "1.0"
        val DEVICE_TYPE = "android"
        val authSalt = "Bearer "
        val DATABASE_NAME = "easytoship_db"

        lateinit var appDatabase: AppDatabase
        private var instance: AppManager? = null
        private lateinit var preferences: SharedPreferences
        private lateinit var c: Context
        val downloadingLinksArray = ArrayList<String>()

        var logoutListener: LogoutListener? = null
        var videoFileDownloaderCallbacks: VideoFileDownloaderCallbacks? = null

        fun getInstance(): AppManager? {
            return instance
        }

        @Synchronized
        fun setInstance(instance: AppManager) {
            if (Companion.instance == null)
                Companion.instance = instance
        }

//        fun getPreferences(context: Context): SharedPreferences? {
//            if (preferences == null)
//                preferences = context.applicationContext.getSharedPreferences(
//                    PrefKeys.PREF_NAME,
//                    Context.MODE_PRIVATE
//                )
//            return preferences
//        }

        private var sUser: User? = null

        fun getUser(): User? {
            if (sUser == null && preferences == null) return null
            else if (sUser == null) {
                preferences?.apply {
                    if (this.contains(PrefKeys.PREF_USER)) {
                        try {
                            val s = this.getString(PrefKeys.PREF_USER, null)
                            sUser = Gson().fromJson<User>(s, User::class.java)
                        } catch (e: Exception) {
                            sUser = null
                        }
                    }
                }
                return sUser
            }
            return sUser
        }

        fun setUser(user: User?) {
            preferences?.apply {
                this.edit().putString(PrefKeys.PREF_USER, Gson().toJson(user)).apply()
            }
            sUser = user
        }

        fun isUserLoggedIn(): Boolean {
            //        return getUser() != null && getUser().getToken() != null;
            return getUser() != null
        }

        // There is no need to set device id because AppManager will automatically generate it (That's why a val)
        //TODO currently device id needed to be hardcoded for testing purpose so...
//        val deviceID: String = "0DE5116F-BE4C-4A0C-8024-BEFB999D0906"
        val deviceID: String = "28B7FD70-273D-4A38-96F0-5CECBD88CE66"
//        val deviceID: String
//            get() = if (preferences.contains(PrefKeys.PREF_DEVICE_ID))
//                preferences.getString(PrefKeys.PREF_DEVICE_ID, null) ?: ""
//            else UUID.randomUUID().toString().toUpperCase()?.apply {
//                preferences.edit().putString(PrefKeys.PREF_DEVICE_ID, this).apply()
//            }

        val context: Context
            get() {
                return c
            }

        fun getString(@StringRes str: Int): String {
            return c.getString(str)
        }

        fun isNetworkConnectedAvailable(): Boolean {
            return Utils.isNetworkConnectedAvailable(c)
        }

        fun getSavedLoginEmail(): String {
            return preferences.getString(PrefKeys.PREF_SAVED_EMAIL, null) ?: ""
        }

        fun getSavedLoginPassword(): String {
            return preferences.getString(PrefKeys.PREF_SAVED_PASSWORD, null) ?: ""
        }

        fun saveLoginEmail(email: String) {
            preferences.edit().putString(PrefKeys.PREF_SAVED_EMAIL, email).apply()
        }

        fun saveLoginPassword(password: String) {
            preferences.edit().putString(PrefKeys.PREF_SAVED_PASSWORD, password).apply()
        }

        fun getNumberOfTicketFooterImageParts() =
            preferences.getInt(PrefKeys.PREF_FOOTER_PARTS, 0)

        fun setNumberOfTicketFooterImageParts(partCount: Int) =
            preferences.edit().putInt(PrefKeys.PREF_FOOTER_PARTS, partCount).apply()

        fun getIsAppSecured() =
            preferences.getBoolean(PrefKeys.PREF_IS_APP_SECURED, false)

        fun setAppSecured(isSecured: Boolean) =
            preferences.edit().putBoolean(PrefKeys.PREF_IS_APP_SECURED, isSecured).apply()

        var isOptionalAppUpdateDialogShown: Boolean = false

        fun getAppExternalStorageFolderPath(): String? {
            val path = context.getExternalFilesDir(null)
                .toString() + File.separator + context.getString(R.string.app_name) + File.separator
//            val path =
//                (Environment.getExternalStorageDirectory().toString()
//                        + File.separator + context.getString(R.string.app_name) + File.separator)
            val folder = File(path)
            if (!folder.exists()) folder.mkdirs()
            return path
        }

        fun getOutputFolderPathForVideos(): String {
            val f = File(AppManager.getAppExternalStorageFolderPath() + "Videos")
            if (!f.exists()) f.mkdirs()
            return f.path
        }

        fun getOutputFolderPathForPhotos(): String {
            val f = File(AppManager.getAppExternalStorageFolderPath() + "Photos")
            if (!f.exists()) f.mkdirs()
            return f.path
        }

        fun saveVideoDownload(videoDownload: VideoDownload) {
            GlobalScope.launch {
                appDatabase.videoDownloadDao().insert(videoDownload)
            }
        }



//        fun getAppExternalStorageFolderPath(): String? {
//            val path: String = (Environment.getExternalStorageDirectory().toString()
//                    + File.separator + context.getString(R.string.app_name) + File.separator)
//            val folder = File(path)
//            if (!folder.exists()) folder.mkdirs()
//            return path
//        }
    }

    interface ServerErrorCodes {
        companion object {
            const val UPDATE_APP = 402
            const val UPDATE_APP_OPTIONAL = 405
            const val SUBSCRIPTION_EXPIRED = 403
            const val USER_TOKEN_EXPIRED = 401
            const val MAINTENANCE_TOKEN = 503
            const val UNKNOWN = -1991
        }
    }

    interface LogoutListener {
        fun logout()
        fun onSubscriptionExpired()
    }

    interface VideoFileDownloaderCallbacks {
        //        val STATUS_DOWNLOADING = 7
//        val STATUS_COMPLETED = 8
//        val STATUS_FAILED = 9
//        val STATUS_PROGRESS = 10
        fun onDownloadingStarted(videoDownload: VideoDownload)
        fun onDownloadingCompleted(videoDownload: VideoDownload)
        fun onDownloadingFailed(videoDownload: VideoDownload)
        fun onDownloadingProgress(videoDownload: VideoDownload, progress: Int)
    }
}