package com.tws.courier.ui.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.ActivityPaymentBinding
import com.tws.courier.domain.base.BaseActivity
import com.tws.courier.domain.models.User
import org.json.JSONObject

class PaymentActivity  : BaseActivity<PaymentViewModel, ActivityPaymentBinding>(),
    PaymentResultListener
{
    override fun getViewModelClass(): Class<PaymentViewModel> = PaymentViewModel::class.java

    override fun getLayoutResource(): Int = R.layout.activity_payment

    private var amt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout().setKeyID("CsfQfz1leFnKja")
        getIntentExtras()
        startPayment()

    }


    companion object {

        val TAG = "PaymentActivity"
        val ARG_AMOUNT = "amt"

        fun createIntentLauncher(context: Context, amt: String) =
            Intent(context, PaymentActivity::class.java)?.apply {
                putExtra(ARG_AMOUNT, amt)
            }
    }

    private fun getIntentExtras() {
        intent?.extras?.let {
            if (it.containsKey(ARG_AMOUNT)) amt = it.getString(ARG_AMOUNT)
        }
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        var user: User? = AppManager.getUser()
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name",getString(R.string.app_name))
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image",R.drawable.app_luncher)
            options.put("currency","INR")
            //options.put("amount",(mTotal*100).toString())
            options.put("amount",amt)

            val prefill = JSONObject()
            prefill.put("email", user?.email)
            prefill.put("contact", user?.mobile)
            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onPaymentError(p0: Int, p1: String?) {
        Log.e("PAYMENT_ERROR_LOG",p0.toString()+"    "+p1.toString())
        p1?.let {
            Toast.makeText(this,p1, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.e("PAYMENT_LOG",p0.toString())
        val subjectArray= JsonArray()
        /*val list = subList.filter { it.selected }
        list.forEach {
            subjectArray.add( JsonObject().apply { addProperty(API_KEY_SUBJECT_ID, it.id) })
        }*/
        val map= JsonObject()
        /*map.add(API_KEY_SUBJECT_ARRAY,subjectArray)
        map.addProperty(API_KEY_TRANSACTION_ID,p0.toString())
        mViewModel.updatePaymentStatus(map)*/
    }
}