package com.tws.courier.domain.download

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import com.app.monrotv.domain.models.VideoDownload
import com.tws.courier.AppManager
import com.tws.courier.domain.notify.NotifyManager
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


public class VideoFileDownloaderService : Service() {

    private lateinit var videoDownload: VideoDownload

    companion object {

        val ARG_VIDEO_DOWNLOAD = "734gdsd6"

        /*fun downloadVideo(activity: Activity, movieDetails: MovieDetails) {
            movieDetails.videoLink?.let { link ->
                if (!TextUtils.isEmpty(link.readyUrl) && !TextUtils.isEmpty(link.cloudflareUrl)) {
                    if (AppManager.downloadingLinksArray.contains(link.readyUrl)) {
                        // Already downloading
                    } else {
                        // Download
                        activity.startService(
                            Intent(activity, VideoFileDownloaderService::class.java).apply {
                                action = movieDetails.id.toString().plus(movieDetails.title)
                                putExtra(
                                    ARG_VIDEO_DOWNLOAD, VideoDownload(
                                        videoId = movieDetails.id,
                                        videoTitle = movieDetails.title,
                                        videoThumb = movieDetails.thumbnailPath,
                                        videoType = movieDetails.type,
                                        videoUrl = link.readyUrl!!,
                                        duration = movieDetails.duration,
                                        userId = AppManager.getUser()?.id ?: 0
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        fun downloadVideo(activity: Activity, episode: Episode) {
            episode.videoLink?.let { link ->
                if (!TextUtils.isEmpty(link.readyUrl) && !TextUtils.isEmpty(link.cloudflareUrl)) {
                    if (AppManager.downloadingLinksArray.contains(link.readyUrl)) {
                        // Already downloading
                    } else {
                        // Download
                        activity.startService(
                            Intent(activity, VideoFileDownloaderService::class.java).apply {
                                action = episode.id.toString().plus(episode.title)
                                putExtra(
                                    ARG_VIDEO_DOWNLOAD, VideoDownload(
                                        videoId = episode.id,
                                        videoTitle = episode.title,
                                        videoThumb = episode.thumbnailPath,
                                        videoType = episode.type,
                                        videoUrl = link.readyUrl!!,
                                        duration = episode.duration,
                                        userId = AppManager.getUser()?.id ?: 0
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }*/
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { intent0 ->
            intent0.extras?.let { extras ->
                videoDownload = extras.getParcelable(ARG_VIDEO_DOWNLOAD)!!
                startServiceInForeground()
                Thread { downloadFileUsingURLStreamConnection() }.start()
//                Thread { downloadFileUsingURLConnection() }.start()
//                Thread { downloadFileUsingFFMpeg() }.start()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startServiceInForeground() {
        startForeground(
            videoDownload.id.toInt(),
            NotifyManager(applicationContext).createVideoDownloadNotification(
                videoDownload.videoTitle, "Downloading...   0%"
            )
        )
    }

    private fun downloadFileUsingURLStreamConnection() {
        try {
            val u = URL(videoDownload.videoUrl)
            val `is` = u.openStream()
            val huc: HttpURLConnection = u.openConnection() as HttpURLConnection //to know the size of video

            val size: Int = huc.getContentLength()

            if (huc != null) {
                videoFile = File(filesDir, getFileNameWithExtension()).apply {
                    if (exists()) delete()
                }
                val fos = FileOutputStream(videoFile)
                val buffer = ByteArray(1024)
                var len1 = 0
                if (`is` != null) {
                    while (`is`.read(buffer).also({ len1 = it }) > 0) {
                        fos.write(buffer, 0, len1)
                    }
                }
                fos?.close()
            }
            onVideoDownloadComplete()
        } catch (e: IOException) {
            e.printStackTrace()
            onVideoDownloadFailed()
        }
    }

    private fun downloadFileUsingURLConnection() {
        try {
            val url = URL(videoDownload.videoUrl)
            val connection = url.openConnection()
            connection.connectTimeout = 60000
            connection.connect()
            // this will be useful so that you can show a typical 0-100% progress bar
            val fileLength = connection.contentLength
            // download the file
            val input: InputStream = BufferedInputStream(connection.getInputStream())

            videoFile = File(filesDir, getFileNameWithExtension()).apply {
                if (exists()) delete()
            }
            val output: OutputStream = FileOutputStream(videoFile)
            val data = ByteArray(1024)
            var total: Long = 0
            var count: Int
            onVideoDownloadingStarted()
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                // publishing the progress....
                val progress = (total * 100 / fileLength).toInt()
                onVideoDownloadProgress(progress)
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
            onVideoDownloadComplete()
        } catch (e: IOException) {
            e.printStackTrace()
            onVideoDownloadFailed()
        }
    }

//    private fun downloadFileUsingFFMpeg() {
//        if (FFmpeg.getInstance(this).isSupported()) {
//            videoFile = File(filesDir, getFileNameWithExtension()).apply {
//                if (exists()) delete()
//            }
//            val cmdArr = arrayOf(
//                "-i",
//                videoDownload.videoUrl.replace("https", "http"),
//                "-c",
//                "copy",
//                "-bsf:a",
//                "aac_adtstoasc",
//                videoFile?.path ?: ""
//            )
//
////            ffmpeg - i "http://example.com/video_url.m3u8"-c copy-bsf:a aac_adtstoasc "output.mp4"
////            val cmd = java.lang.String.format(
////                "-i %s -acodec %s -bsf:a aac_adtstoasc -vcodec %s %s",
////                INPUT_FILE,
////                "copy",
////                "copy",
////                dir.toString().toString() + "/bigBuckBunny.mp4"
////            )
////            val command = cmd.split(" ").toTypedArray()
//
//            FFmpeg.getInstance(this).execute(cmdArr, object : ExecuteBinaryResponseHandler() {
//                override fun onStart() {
//                    onVideoDownloadingStarted()
//                }
//
//                override fun onProgress(message: String) {
//                    print("msgs : " + message)
//                }
//
//                override fun onFailure(message: String) {
//                    onVideoDownloadFailed()
//                }
//
//                override fun onSuccess(message: String) {
//                    onVideoDownloadComplete()
//                }
//
//                override fun onFinish() {}
//            })
//        } else {
//            onVideoDownloadFailed()
//        }
//    }

    private fun getFileExtension() =
        videoDownload.videoUrl.substring(startIndex = videoDownload.videoUrl.lastIndexOf("."))

    private fun getFileNameWithExtension() =
        videoDownload.videoId.toString().plus(videoDownload.videoType)
//            .plus(getFileExtension())
            .plus(".mp4")

    private var videoFile: File? = null

    private fun onVideoDownloadingStarted() {
        AppManager.videoFileDownloaderCallbacks?.onDownloadingStarted(videoDownload)
    }

    private fun onVideoDownloadProgress(progress: Int) {
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(
                videoDownload.id.toInt(),
                NotifyManager(applicationContext).createVideoDownloadNotification(
                    videoDownload.videoTitle,
                    "Downloading...   ".plus(progress.toString()).plus("%")
                )
            )
        }
        AppManager.videoFileDownloaderCallbacks?.onDownloadingProgress(videoDownload, progress)
    }

    private fun onVideoDownloadComplete() {
        AppManager.videoFileDownloaderCallbacks?.onDownloadingCompleted(videoDownload)
        stopSelf()
    }

    private fun onVideoDownloadFailed() {
        videoFile?.let {
            if (it.exists()) it.delete()
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(
                videoDownload.id.toInt(),
                NotifyManager(applicationContext).createVideoDownloadNotification(
                    videoDownload.videoTitle,
                    "Downloading failed!")
                )
        }
        AppManager.videoFileDownloaderCallbacks?.onDownloadingFailed(videoDownload)
        stopSelf()
    }
}