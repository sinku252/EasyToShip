package com.tws.courier.ui.account_setting

import androidx.lifecycle.MutableLiveData
import com.tws.courier.AppManager
import com.tws.courier.data.RepoListener
import com.tws.courier.data.remote.ApiClient
import com.tws.courier.data.remote.RemoteRepo
import com.tws.courier.domain.annotations.DataRequestType
import com.tws.courier.domain.base.BaseViewModel
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.model.remote.response.BaseResponse
import com.tws.courier.domain.models.Address
import com.tws.courier.domain.models.Bank
import com.tws.courier.domain.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class AccountSettingViewModel : BaseViewModel()
{
    val showView = MutableLiveData<SingleActionEvent<Int>>()
    val onAddressListReceived = MutableLiveData<SingleActionEvent<List<Address>>>()
    val onAddressDelete = MutableLiveData<SingleActionEvent<String>>()
    val navigateToAddress = MutableLiveData<SingleActionEvent<Boolean>>()
    val generalDetails = MutableLiveData<SingleActionEvent<User>>()
    val bankDetails = MutableLiveData<SingleActionEvent<Bank>>()
    val updateGeneralDetails = MutableLiveData<SingleActionEvent<String>>()
    val updateBankDetails = MutableLiveData<SingleActionEvent<String>>()






    public fun selectLayout(type:Int)
    {
        showView.value = SingleActionEvent(type)
    }

    fun getGeneral()
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "get_general"
        map["seller_type"] = "customer"//"agent"

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<User>> {
                        override val deferred: Deferred<BaseResponse<User>>
                            get() = ApiClient.authorizedApiService.getGeneral(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_GENERAL
                        override val repoListener: RepoListener
                            get() = this@AccountSettingViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                    {
                        AppManager.setUser(this.response.data)
                        generalDetails.value = SingleActionEvent(this.response.data)
                    }
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }
    fun getBank()
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "get_bank"
        map["seller_type"] = "customer"//"agent"

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Bank>> {
                        override val deferred: Deferred<BaseResponse<Bank>>
                            get() = ApiClient.authorizedApiService.getBank(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_BANK
                        override val repoListener: RepoListener
                            get() = this@AccountSettingViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        bankDetails.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }

    fun updateGeneralDetails(user: User?)
    {
        val map = HashMap<String, String>()
        if(user!=null)
        {
            map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
            map["method_name"] = "add_general_details"
            map["seller_type"] = "customer"//"agent"
            map["company_name"] = user?.company_name
            map["username"] = user.username
            map["city"] = user.city
            map["email"] = user.email
            map["gst_no"] = user.gst_no
            map["pan_no"] = user.pan_no
            map["mobile"] = user.mobile
        }



        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.authorizedApiService.addGeneralDetails(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.ADD_GENERAL_DETAILS
                        override val repoListener: RepoListener
                            get() = this@AccountSettingViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        updateGeneralDetails.value = SingleActionEvent(this.response.message)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }


    }

    fun updateBankDetails(bank: Bank?)
    {


        val map = HashMap<String, String>()
        if(bank!=null) {
            map["user_id"] = AppManager.getUser()?.id ?: ""//user_id.trim()
            map["method_name"] = "add_bank_details"
            map["seller_type"] = "customer"//"agent"
            map["bank_name"] = bank.bankName
            map["branch_name"] = bank.branchName
            map["branch_city"] = bank.branchCity
            map["account_no"] = bank.accountNo
            map["account_type"] = bank.accountType
            map["ifsc_code"] = bank.ifscCode
            map["beneficiary_name"] = bank.beneficiaryName
        }

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.authorizedApiService.addBankDetails(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.ADD_BANK_DETAILS
                        override val repoListener: RepoListener
                            get() = this@AccountSettingViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        updateBankDetails.value = SingleActionEvent(this.response.message)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }




    fun requestAddress(user_id: String) {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "my_address_list"
        map["seller_type"] = "customer"//"agent"

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<List<Address>>> {
                        override val deferred: Deferred<BaseResponse<List<Address>>>
                            get() = ApiClient.authorizedApiService.addressList(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.GET_ADDRESS_LIST
                        override val repoListener: RepoListener
                            get() = this@AccountSettingViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onAddressListReceived.value = SingleActionEvent(this.response.data)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


    fun deleteAddress(address: Address)
    {
        val map = HashMap<String, String>()
        map["user_id"]  = AppManager.getUser()?.id ?: ""//user_id.trim()
        map["method_name"] = "delete_myaddress"
        map["seller_type"] = "customer"//"agent"
        map["address_id"] = address.id

        uiScope.launch {
            (
                    object : RemoteRepo<BaseResponse<Any>> {
                        override val deferred: Deferred<BaseResponse<Any>>
                            get() = ApiClient.authorizedApiService.deleteAddress(map)
                        override val dataRequestType: Int
                            get() = DataRequestType.DELETE_ADDRESS
                        override val repoListener: RepoListener
                            get() = this@AccountSettingViewModel.repoListener
                    }
                    )
                .executeApiRequest()?.apply {
                    if (this.response.status.equals("1", ignoreCase = true))
                        onAddressDelete.value = SingleActionEvent(this.response.message)
                    else showToast.value = SingleActionEvent(this.response.message)
                }
        }
    }


    fun addAddress() {
        closeKeyBoard()
        navigateToAddress.value = SingleActionEvent(true)
    }


}