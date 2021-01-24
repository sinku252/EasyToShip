package com.app.monrotv.ui.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.tws.courier.CourierApp
import com.tws.courier.ui.demo.DemoDownloadService

class NotifyDownloadActionsReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_CANCEL_DOWNLOADS = "action.downloads.cancel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action != null && intent.action
            == ACTION_CANCEL_DOWNLOADS
        ) {
            try {
                (context.applicationContext as CourierApp)?.let { app ->
//                    app.downloadTracker.re
//                    ((DemoApplication) getApplication()).getDownloadManager().pauseDownloads();
//                    ((DemoApplication) getApplication()).getDownloadManager().removeAllDownloads();
//                    ((DemoApplication) getApplication()).getDownloadManager().resumeDownloads();
                    app.downloadManager.pauseDownloads()
                    DemoDownloadService.videoDownloads.forEach {
                        app.downloadManager.removeDownload(it.videoUrl)
                    }
                    DemoDownloadService.videoDownloads.clear()
                    app.downloadManager.resumeDownloads()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
