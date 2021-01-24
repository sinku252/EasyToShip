package com.tws.courier.domain.base

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.domain.utils.FileUtils
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

abstract class MediaOptionsFragment<T : BaseViewModel, B : ViewDataBinding> : BaseFragment<T, B>() {

    abstract fun onCameraPermissionsDenied()
    abstract fun onPhotoReceived(path: String)
//    abstract fun onVideoReceived(path: String)

    private val REQUEST_TAKE_PHOTO = 1
    private val REQUEST_CAPTURE_VIDEO = 2
    private val MAX_VIDEO_DURATION = 20 // in seconds



    fun pickPhotoOrVideoFromGallery() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Gallery") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
            .includeVideo(true) // Show video on image picke
            .single() // single mode
//            .multi() // multi mode (default mode)
//            .limit(10) // max images can be selected (99 by default)
            .showCamera(false) // show camera or not (true by default)
            //                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
            //                .origin(images) // original selected images, used in multi mode
            //                .exclude(images) // exclude anything that in image.getPath()
            //                .excludeFiles(files) // same as exclude but using ArrayList<File>
            .theme(R.style.AppTheme_NoActionBar) // must inherit ef_BaseTheme. please refer to sample
            .enableLog(false) // disabling log
            .start() // start image picker activity with request code
    }

    fun pickVideoFromGallery() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Gallery") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
            .includeVideo(true) // Show video on image pick
            .onlyVideo(true)
            .single() // single mode
            .showCamera(false) // show camera or not (true by default)
            .theme(R.style.AppTheme_NoActionBar) // must inherit ef_BaseTheme. please refer to sample
            .enableLog(false) // disabling log
            .start() // start image picker activity with request code
    }

    fun pickPhotoFromGallery() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Gallery") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
            .includeVideo(false) // Show video on image pick
            .single() // single mode
            .showCamera(false) // show camera or not (true by default)
            .theme(R.style.AppTheme_NoActionBar) // must inherit ef_BaseTheme. please refer to sample
            .enableLog(false) // disabling log
            .start() // start image picker activity with request code
    }

//    fun captureVideo() {
//        activity?.let { activity ->
//            Dexter.withActivity(activity)
//                .withPermissions(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                        report?.let {
//                            if (it.areAllPermissionsGranted()) {
////                                dispatchTakeVideoIntent()
//                            } else {
//                                context?.let { context ->
//                                    AlertDialog.Builder(context)
//                                        .setMessage(getString(R.string.warn_camera_permissions_denied))
//                                        .setPositiveButton(
//                                            getString(R.string.yes),
//                                            DialogInterface.OnClickListener { dialog, which ->
//                                                dialog.dismiss()
//                                                capturePhoto()
//                                            })
//                                        .setNegativeButton(getString(R.string.no),
//                                            DialogInterface.OnClickListener { dialog, which ->
//                                                dialog.dismiss()
//                                                onCameraPermissionsDenied()
//                                            }).create().show()
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(
//                        permissions: MutableList<PermissionRequest>?,
//                        token: PermissionToken?
//                    ) {
//                        context?.let { context ->
//                            AlertDialog.Builder(context)
//                                .setMessage(getString(R.string.rationale_camera_permissions))
//                                .setNeutralButton(
//                                    getString(R.string.ok),
//                                    DialogInterface.OnClickListener { dialog, which ->
//                                        dialog.dismiss()
//                                        token?.continuePermissionRequest()
//                                    })
//                                .create().show()
//                        }
//                    }
//                }).check()
//        }
//    }

    fun capturePhoto() {
        activity?.let { activity ->
            Dexter.withActivity(activity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.areAllPermissionsGranted()) {
                                dispatchTakePictureIntent()
                            } else {
                                context?.let { context ->
                                    AlertDialog.Builder(context)
                                        .setMessage(getString(R.string.warn_camera_permissions_denied))
                                        .setPositiveButton(
                                            getString(R.string.yes),
                                            DialogInterface.OnClickListener { dialog, which ->
                                                dialog.dismiss()
                                                capturePhoto()
                                            })
                                        .setNegativeButton(getString(R.string.no),
                                            DialogInterface.OnClickListener { dialog, which ->
                                                dialog.dismiss()
                                                onCameraPermissionsDenied()
                                            }).create().show()
                                }
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        context?.let { context ->
                            AlertDialog.Builder(context)
                                .setMessage(getString(R.string.rationale_camera_permissions))
                                .setNeutralButton(
                                    getString(R.string.ok),
                                    DialogInterface.OnClickListener { dialog, which ->
                                        dialog.dismiss()
                                        token?.continuePermissionRequest()
                                    })
                                .create().show()
                        }
                    }
                }).check()
        }
    }

    fun checkCameraPermission() {

    }

    private fun dispatchTakePictureIntent() {
        activity?.let { activity ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity.packageManager)?.also {
                    val photoFile: File? = createMediaFile(TYPE_FILE_PHOTO)
                    photoFile?.also {
                        currentPhotoUri = FileProvider.getUriForFile(
                            activity,
                            activity.getApplicationContext().getPackageName() + ".fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
        }
    }

//    fun dispatchTakeVideoIntent() {
//        activity?.let { activity ->
//            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { videoIntent ->
//                videoIntent.resolveActivity(activity.packageManager)?.also {
//                    val videoFile: File? = createMediaFile(TYPE_FILE_VIDEO)
//                    videoFile?.also {
//                        currentVideoUri = FileProvider.getUriForFile(
//                            activity,
//                            activity.getApplicationContext().getPackageName() + ".fileprovider",
//                            it
//                        )
//                        videoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, MAX_VIDEO_DURATION)
////            videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
//                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentVideoUri)
//                        startActivityForResult(videoIntent, REQUEST_CAPTURE_VIDEO)
//                    }
//                }
//            }
//        }
//    }

    private var currentPhotoPath: String? = null
    private var currentPhotoUri: Uri? = null
    private var currentVideoPath: String? = null
//    private var currentVideoUri: Uri? = null

    val TYPE_FILE_PHOTO = 34
    val TYPE_FILE_VIDEO = 35

    fun createMediaFile(type: Int): File? {
        try {
            activity?.let { activity ->
                val timeStamp: String = SimpleDateFormat("dd_MMM_yyyy__hh_mm_a").format(Date())
                if (type == TYPE_FILE_PHOTO) {
                    val file =
                        File(AppManager.getOutputFolderPathForPhotos() + File.separator + "IMG_${timeStamp}_" + ".jpg").apply {
                            currentPhotoPath = absolutePath
                        }
                    file.createNewFile()
                    return file
                }
                if (type == TYPE_FILE_VIDEO) {
                    val file =
                        File(AppManager.getOutputFolderPathForVideos() + File.separator + "VID_${timeStamp}_" + ".mp4").apply {
                            currentVideoPath = absolutePath
                        }
                    file.createNewFile()
                    return file
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showMessageDialog("Unable to create file! Please check your storage space.")
            return null
        }
        return null
    }

    fun createTempMediaFile(type: Int): File? {
        activity?.let { activity ->
            if (type == TYPE_FILE_PHOTO)
                return File.createTempFile(
                    "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                    ".jpg",
                    File(AppManager.getOutputFolderPathForPhotos())
                )
            if (type == TYPE_FILE_VIDEO)
                return File.createTempFile(
                    "VID_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                    ".mp4",
                    File(AppManager.getOutputFolderPathForVideos())
                )
        }
        return null
    }

    private fun galleryAddPic() {
        activity?.let {
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                val f = File(currentPhotoPath)
                mediaScanIntent.data = Uri.fromFile(f)
                it.sendBroadcast(mediaScanIntent)
            }
        }
    }

    private fun galleryAddVideo() {
        activity?.let {
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                val f = File(currentVideoPath)
                mediaScanIntent.data = Uri.fromFile(f)
                it.sendBroadcast(mediaScanIntent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ImagePicker.getImages(data)?.let { images ->
                images.get(0)?.let { img ->
                    FileUtils.getMimeType(File(img.path))?.let { type ->
                        if (type.startsWith("image", true)) {
                            currentPhotoPath = img.path
                            compressAndSendImage()
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_TAKE_PHOTO && resultCode === Activity.RESULT_OK) {
            try {
                galleryAddPic()
                compressAndSendImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
//        if (requestCode == REQUEST_CAPTURE_VIDEO && resultCode === Activity.RESULT_OK) {
//            try {
//                galleryAddVideo()
//                compressAndSendVideo()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
    }

    private fun getVideoFileDuration(): Long {
        var duration = 0L
        MediaMetadataRetriever().let {
            it.setDataSource(context, Uri.fromFile(File(currentVideoPath)))
            duration = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
            it.release()
        }
        return duration
    }

    private fun compressAndSendImage() {
        viewModel.uiScope.launch {
            CoroutineScope(Dispatchers.IO).async {
                context?.let {
                    activity?.runOnUiThread { showProgressDialog("Compressing, Please wait...") }

                    val file = Compressor.compress(it, File(currentPhotoPath))
//                    val compressedImageFile = Compressor.compress(it, File(currentPhotoPath)) {
//                        default()
//                        destination(myFile)
//                    }
//
//                    val filePath: String = SiliCompressor.with(it).compress(
//                        currentPhotoPath,
//                        File(AppManager.getOutputFolderPathForCompressedPhotos())
//                    )
//
//                    val fileUri = FileProvider.getUriForFile(
//                        it,
//                        activity?.getApplicationContext()?.getPackageName() + ".fileprovider",
//                        File(filePath)
//                    )
                    activity?.runOnUiThread { dismissProgressDialog() }
                    onPhotoReceived(file.path)
                }
            }.await()

        }
    }

    var path: String? = null

   }