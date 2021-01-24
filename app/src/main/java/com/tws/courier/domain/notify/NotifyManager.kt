package com.tws.courier.domain.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotifyManager(val context: Context) {

    init {
        createDownloadNotificationChannel()
    }

    companion object {
        // Channels
        val ID_CHANNEL_DOWNLOAD = "345Do"
        // Notification ids

    }

    private fun createDownloadNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download"
            val descriptionText = "Show notification while downloading videos"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ID_CHANNEL_DOWNLOAD, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createVideoDownloadNotification(
        title: String,
        message: String
    ) = NotificationCompat.Builder(context, ID_CHANNEL_DOWNLOAD)
            .setSmallIcon(com.tws.courier.R.mipmap.ic_noti_small_)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false).build()


//    with(NotificationManagerCompat.from(context)) {
//        notify(notificationId, builder.build())
//    }
}