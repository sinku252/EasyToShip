package com.tws.courier

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tws.courier.domain.dialogs.MessageDialog
import com.tws.courier.domain.dialogs.TitledMessageDialog
import com.tws.courier.domain.models.Order
import com.tws.courier.domain.models.OrderCalculation
import com.tws.courier.domain.universal_adapter.RecyclerItem
import com.tws.courier.domain.universal_adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.dialog_add_money.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@set:BindingAdapter("imageUrl")
var ImageView.imageUrl: String?
    get() = imageUrl
    set(value) {
        if (URLUtil.isValidUrl(value))
            Glide.with(context).load(value).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).centerCrop().into(this)
        else Glide.with(context).load(R.drawable.placeholder).centerCrop().into(this)
    }

@set:BindingAdapter("imageUrlBroad")
var ImageView.imageUrlBroad: String?
    get() = imageUrlBroad
    set(value) {
        if (URLUtil.isValidUrl(value))
            Glide.with(context).load(value).placeholder(R.drawable.placeholder_broad)
                .error(R.drawable.placeholder_broad).centerCrop().into(this)
        else Glide.with(context).load(R.drawable.placeholder_broad).centerCrop().into(this)
    }

@set:BindingAdapter("imageUrlWithLoading")
var ImageView.imageUrlWithLoading: String?
    get() = imageUrlWithLoading
    set(value) {
        if (URLUtil.isValidUrl(value)) {
            Glide.with(context)
                .load(value)
                .placeholder(CircularProgressDrawable(context)?.apply {
                    strokeWidth = 5f
                    centerRadius = 30f
                    start()
                })
                .error(R.drawable.placeholder)
                .centerCrop().into(this)
        } else Glide.with(context).load(R.drawable.placeholder).centerCrop().into(this)
    }

@set:BindingAdapter("imageUrlWithLoadingBroad")
var ImageView.imageUrlWithLoadingBroad: String?
    get() = imageUrlWithLoadingBroad
    set(value) {
        if (URLUtil.isValidUrl(value)) {
            Glide.with(context)
                .load(value)
                .placeholder(CircularProgressDrawable(context)?.apply {
                    strokeWidth = 5f
                    centerRadius = 30f
                    start()
                })
                .error(R.drawable.placeholder_broad)
                .centerCrop().into(this)
        } else Glide.with(context).load(R.drawable.placeholder_broad).centerCrop().into(this)
    }

@set:BindingAdapter("imageUrlWithLoadingFitXY")
var ImageView.imageUrlWithLoadingFitXY: String?
    get() = imageUrlWithLoadingFitXY
    set(value) {
        if (URLUtil.isValidUrl(value)) {
            Glide.with(context)
                .load(value)
                .placeholder(CircularProgressDrawable(context)?.apply {
                    strokeWidth = 5f
                    centerRadius = 30f
                    start()
                })
                .error(R.drawable.placeholder).into(this)
        } else Glide.with(context).load(R.drawable.placeholder).centerCrop().into(this)
    }

@set:BindingAdapter("imageLocal")
var ImageView.imageLocal: Int?
    @RawRes @DrawableRes @Nullable get() = imageLocal
    set(@RawRes @DrawableRes @Nullable value) {
        Glide.with(context).load(value).placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder).centerCrop().into(this)
    }

@set:BindingAdapter("imageLocalDefault")
var ImageView.imageLocalDefault: Int?
    @RawRes @DrawableRes @Nullable get() = imageLocalDefault
    set(@RawRes @DrawableRes @Nullable value) {
        Glide.with(context).load(value).placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder).into(this)
    }

@set:BindingAdapter("imageLocalNoPlaceholders")
var ImageView.imageLocalNoPlaceholders: Int?
    @RawRes @DrawableRes @Nullable get() = imageLocalNoPlaceholders
    set(@RawRes @DrawableRes @Nullable value) {
        Glide.with(context).load(value).into(this)
    }

@set:BindingAdapter("visibleOrGone")
var View.visibleOrGone
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

@set:BindingAdapter("visible")
var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }


// Activity extensions

var Activity.showToastShort: String?
    get() = showToastShort
    set(value) {
        if (!TextUtils.isEmpty(value))
            Toast.makeText(this, value, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.BOTTOM, 0, 0);
            }.show()
    }

var Activity.showMessageDialog: String
    get() = showMessageDialog
    set(value) {
        MessageDialog(this, value).show()
//        AlertDialog.Builder(this).apply {
//            setMessage(value)
//            setNeutralButton(
//                getString(R.string.ok),
//                { dialog, which ->
//                    dialog.dismiss()
//                })
//        }.create().show()
    }

fun Activity.closeKeyboard() {
    this.currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.showMessageDialog(
    title: String, message: String,
    positiveClickListener: View.OnClickListener, negativeClickListener: View.OnClickListener
) {
    TitledMessageDialog(
        this,
        title,
        message,
        positiveClickListener = positiveClickListener,
        negativeClickListener = negativeClickListener
    ).show()
}

fun Activity.showMessageDialog(
    title: String,
    message: String,
    okClickListener: View.OnClickListener
) {
    TitledMessageDialog(this, title, message, neutralClickListener = okClickListener).show()
}

// Fragment extensions
var Fragment.showToastShort: String?
    get() = showToastShort
    set(value) {
        if (!TextUtils.isEmpty(value))
            Toast.makeText(context, value, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.BOTTOM, 0, 0);
            }.show()
    }

var Fragment.showMessageDialog: String
    get() = showMessageDialog
    set(value) {
        context?.let {
            MessageDialog(it, value).show()
        }
    }

fun Fragment.showMessageDialog(title: String, message: String) {
    context?.let {
        TitledMessageDialog(it, title, message).show()
    }
}

fun Fragment.showMessageDialog(
    title: String,
    message: String,
    okClickListener: View.OnClickListener
) {
    context?.let {
        TitledMessageDialog(it, title, message, neutralClickListener = okClickListener).show()
    }
}

fun Fragment.showMessageDialog(
    title: String, message: String,
    positiveClickListener: View.OnClickListener, negativeClickListener: View.OnClickListener
) {
    context?.let {
        TitledMessageDialog(
            it,
            title,
            message,
            positiveClickListener = positiveClickListener,
            negativeClickListener = negativeClickListener
        ).show()
    }
}

fun Fragment.closeKeyboard() {
    context?.let { c ->
        view?.let {
            (c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

// Recycler ViewHolder extensions
// Call inside onBindViewHolder with holder.functionName/holder.varName
// Call inside holder class scope with functionName/varName

var RecyclerView.ViewHolder.showToastShort: String?
    get() = showToastShort
    set(value) {
        if (!TextUtils.isEmpty(value))
            Toast.makeText(itemView.context, value, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.BOTTOM, 0, 0);
            }.show()
    }

var RecyclerView.ViewHolder.showMessageDialog: String
    get() = showMessageDialog
    set(value) {
        itemView.context?.let {
            MessageDialog(it, value).show()
//            AlertDialog.Builder(it).apply {
//                setMessage(value)
//                setNeutralButton(
//                    it.getString(R.string.ok),
//                    { dialog, which ->
//                        dialog.dismiss()
//                    })
//            }.create().show()
        }
    }

fun RecyclerView.ViewHolder.closeKeyboard() {
    itemView?.let {
        itemView.context?.let { c ->
            (c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

// TextView extension
fun TextView.setTextOrEmpty(s: String?) = s?.let { text = s } ?: kotlin.run { text = "" }
fun TextView.setTextOrNA(s: String?) = s?.let { text = s } ?: kotlin.run { text = "NA" }

    //region Adding a SafeClick listener (Stop multiple clicks)
class SafeClickListener(
        private var defaultInterval: Int = 1000,
        private val onSafeCLick: (View) -> Unit
    ) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

@BindingAdapter("entries")
fun entries(recyclerView: RecyclerView?, array: Array<String?>?) {
    //get all values in array[] variable.
}

@BindingAdapter("items")
fun setRecyclerViewItems(
    recyclerView: RecyclerView,
    items: List<RecyclerItem>?
) {
    var adapter = (recyclerView.adapter as? RecyclerViewAdapter)
    if (adapter == null) {
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
    }

    adapter.updateData(items.orEmpty())
}

fun getDateWithMonthNameFromCalendar(testDate: String): String {
    // finalDate=SimpleDateFormat("MMM dd, yyyy").format(secondSimpleDateFormat.parse(testDate))
    val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
    val df: DateFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm aaa", Locale.ENGLISH)
    val date: Date = sdf.parse(testDate) // converting String to date
    return df.format(date)
    //System.out.println(df.format(date))
}

fun getDateWithMonthName(testDate: String): String {
    // finalDate=SimpleDateFormat("MMM dd, yyyy").format(secondSimpleDateFormat.parse(testDate))
    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm",
        Locale.ENGLISH
    )
    val df: DateFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm aaa", Locale.ENGLISH)
    val date: Date = sdf.parse(testDate) // converting String to date
    return df.format(date)
    //System.out.println(df.format(date))
}

fun getImageByVehicleType(vehicleType: String):Int
{
    var type:Int=0
    if(vehicleType.equals("bike"))
         type=R.drawable.ic_bike_blue
        //viewBinding.ivVehicle.setImageResource(R.drawable.ic_bike_blue)
    else if(vehicleType.equals("truck"))
        type=R.drawable.ic_truck_blue
       // viewBinding.ivVehicle.setImageResource(R.drawable.ic_truck_blue)
    else if(vehicleType.equals("train"))
        type=R.drawable.ic_train_blue
        //viewBinding.ivVehicle.setImageResource(R.drawable.ic_train_blue)
    else if(vehicleType.equals("flight"))
        type=R.drawable.ic_flight_blue
        //viewBinding.ivVehicle.setImageResource(R.drawable.ic_flight_blue)
    return type
}

fun getObjectByType(areaType: String,vehicleType: String,list:ArrayList<OrderCalculation>): OrderCalculation?
{
    //val indices = list!!.mapIndexedNotNull { index, event ->  if (event.areaType.equals(areaType,ignoreCase = true) && event.type.equals(vehicleType,ignoreCase = true)) index else null}

    /* var orderCalculation = OrderCalculation()
     list!!.forEach { event ->
         if(event.areaType !=null && TextUtils.isEmpty(event.areaType.trim()))
         {
             if (event.areaType.equals(areaType,ignoreCase = true) && event.type.equals(vehicleType,ignoreCase = true)) {
                 orderCalculation = event
                 Log.e("fasf","1256666")
             }
             Log.e("fasf","125")
         }
         else
         {
             if (event.type.equals(vehicleType,ignoreCase = true)) {
                 orderCalculation = event
             }
             Log.e("fasf","123")
         }
     }*/
    var orderCalculation: OrderCalculation? = list.find {
   //     TextUtils.isEmpty(it.areaType.trim()) ? it.type.equals(vehicleType,ignoreCase = true) : it.areaType.equals(areaType,ignoreCase = true)
        it.type.equals(vehicleType,ignoreCase = true) &&  it.areaType.equals(areaType,ignoreCase = true)

    }
  //  indices.forEach { println(it) }
    return orderCalculation

}

fun Context.showAddMoneyDialog(success: (text: String) -> Unit): Dialog {
    return Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen).apply {
       // requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_money)
        show()
        add_money.setOnClickListener {
            success(et_amt.text.toString())
        }
        /*add_money.setOnClickListener(CustomClickListener {
            success()
            dismiss()
        })*/
    }
}

 fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, unit: String): Double {
     if (lat1 == lat2 && lon1 == lon2) {
        return 0.0
    } else {
        val theta = lon1 - lon2
        var dist =
            Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(
                lat1)) * Math.cos(
                Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta))
        dist = Math.acos(dist)
        dist = Math.toDegrees(dist)
        dist = dist * 60 * 1.1515
        if (unit == "K") {
            dist = dist * 1.609344
        } else if (unit == "N") {
            dist = dist * 0.8684
        }
         return dist
    }
}
fun getKmFromLatLong(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val Rad = 6372.8 //Earth's Radius In kilometers

    // TODO Auto-generated method stub
    // TODO Auto-generated method stub
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lat2 - lng1)
   /* lat1 = Math.toRadians(lat1)
    lat2 = Math.toRadians(lat2)*/
    val a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(
            Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
    val c = 2 * Math.asin(Math.sqrt(a))
    Log.e("Gaasg",""+(Rad * c))
    return Rad * c
   // haverdistanceKM = Rad * c
    /*val loc1 = Location("")
    loc1.setLatitude(lat1)
    loc1.setLongitude(lng1)
    val loc2 = Location("")
    loc2.setLatitude(lat2)
    loc2.setLongitude(lng2)
    val distanceInMeters: Float = loc1.distanceTo(loc2)
    Log.e("ffaf",""+(distanceInMeters / 1000))
    return distanceInMeters / 1000*/
}

// endregion