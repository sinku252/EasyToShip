package com.tws.courier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tws.courier.domain.utils.KotlinPreferencesHelper
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService()
{
    private val TAG = "MyFirebaseMessagingService"
    private lateinit var notificationManager: NotificationManager
    private lateinit var ADMIN_CHANNEL_ID:String
    var mPreference: KotlinPreferencesHelper? = null
    override fun onNewToken(refreshedToken: String) {
        Log.e("NEW_TOKEN", refreshedToken)
        ADMIN_CHANNEL_ID= this.getString(R.string.app_name)
        mPreference = KotlinPreferencesHelper(this)
        mPreference!!.fcmToken=refreshedToken
        mPreference!!.isTokenSave=false
        //KotlinPreferencesHelper
        /*AppUtilities.writeToPref(IPreferences.FCM_REGISTRATION_TOKEN, refreshedToken)
        AppUtilities.writeBooleanToPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, false)
        sendRegistrationToServer(refreshedToken)*/

    }
    /*fun onNewToken(refreshedToken: String?) {
        Log.e("NEW_TOKEN", refreshedToken)
        AppUtilities.writeToPref(IPreferences.FCM_REGISTRATION_TOKEN, refreshedToken)
        AppUtilities.writeBooleanToPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, false)
        sendRegistrationToServer(refreshedToken)
    }*/

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage?.let { message ->
            Log.i(TAG, message.getData().get("message"))

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //Setting up Notification channels for android O and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupNotificationChannels()
            }
            val notificationId = Random().nextInt(60000)

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_luncher)  //a resource for your custom small icon
                .setContentTitle(message.data["title"]) //the "title" value you sent in your notification
                .setContentText(message.data["message"]) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build())

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {
        val adminChannelName = getString(R.string.notifications_admin_channel_name)
        val adminChannelDescription = getString(R.string.notifications_admin_channel_description)

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager.createNotificationChannel(adminChannel)
    }
}