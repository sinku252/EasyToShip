package com.tws.courier.data.remote

import android.text.TextUtils
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.data.RepoListener
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.model.remote.response.BaseResponse
import kotlinx.coroutines.Deferred
import retrofit2.HttpException


interface RemoteRepo<T> {

    val deferred: Deferred<T>

    @DataRequestType
    val dataRequestType: Int
    val repoListener: RepoListener

    suspend fun executeApiRequest(): T? {
        if (AppManager.isNetworkConnectedAvailable()) {
            try {
                repoListener.setDataRequestProgressIndicator(dataRequestType, true)
                val t: T = deferred.await()
                if (t is BaseResponse<*>) {
                    if (t.response.status.equals("1", ignoreCase = true))
                    //if (t.status)
                        repoListener.onDataRequestSucceed(dataRequestType, t)
                    else {
                        var message = t.response.message
                        if (TextUtils.isEmpty(t.response.message))
                            message = AppManager.context.getString(R.string.err_something_wrong)
                        /*if (t.code == AppManager.ServerErrorCodes.SUBSCRIPTION_EXPIRED)
                            message = ""*/
                        repoListener.onDataRequestFailed(dataRequestType, 1001, message)
                    }
                } else repoListener.onDataRequestSucceed(dataRequestType, t)
                repoListener.setDataRequestProgressIndicator(dataRequestType, false)
                return t
            } catch (httpException: HttpException) {
                // a non-2XX response was received
                repoListener.onDataRequestFailed(
                    dataRequestType,
                    httpException.code(),
                    httpException.message() ?: AppManager.getString(R.string.err_something_wrong)
                )
                repoListener.setDataRequestProgressIndicator(dataRequestType, false)
                return null
            } catch (t: Throwable) {
                // a networking or data conversion error
                repoListener.onDataRequestFailed(
                    dataRequestType,
                    ERROR_CODE_FAILED_TO_CONNECT,
                    AppManager.getString(R.string.err_something_wrong)
                )
                repoListener.setDataRequestProgressIndicator(dataRequestType, false)
                return null
            }
        } else {
            // No network connections available error
            repoListener.onNetworkConnectionError(
                dataRequestType,
                AppManager.getString(R.string.err_network_connection)
            )
            repoListener.setDataRequestProgressIndicator(dataRequestType, false)
            return null
        }
    }

    companion object {
        const val ERROR_CODE_FAILED_TO_CONNECT = -7131313
    }
}