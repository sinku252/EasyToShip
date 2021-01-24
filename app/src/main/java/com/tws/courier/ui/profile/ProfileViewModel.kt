package com.tws.courier.ui.profile

import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ProfileViewModel : BaseViewModel()
{
    val onUpdateProfileImage = MutableLiveData<SingleActionEvent<User>>()
    val onlogoutRecived = MutableLiveData<SingleActionEvent<Boolean>>()

    var selectedFilePath: String? = null


    fun logout()
    {
        onlogoutRecived.value = SingleActionEvent(true)
    }

    fun requestUpdateUserProfile() {

       var userId= AppManager.getUser()?.id ?: ""

        val nameBody: RequestBody =
            userId.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
           var photoFileBody: MultipartBody.Part? = null

        selectedFilePath?.let { filePath ->
            val file = File(filePath)
            photoFileBody =
                MultipartBody.Part.createFormData(
                    "image",
                    file.getName(),
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
        }




        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<User>> {
                        override val deferred: Deferred<BaseResponse<User>>
                            get() = ApiClient.apiUploadService.updateUserProfile(
                                user_id = nameBody,
                                profile_pic = photoFileBody
                            )
                        override val dataRequestType: Int
                            get() = DataRequestType.UPDATE_PROFILE_IMAGE
                        override val repoListener: RepoListener
                            get() = this@ProfileViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true)) {
                        showToast.value = SingleActionEvent(this.response.message)
                        onUpdateProfileImage.value = SingleActionEvent(this.response.data)
                    } else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }
}