package com.tws.courier.domain.utils


import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tws.courier.CourierApp.gson
import com.tws.courier.domain.models.OrderCalculation
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Pranay on 6/9/2017.
 */
class KotlinPreferencesHelper /*internal constructor(val sharedPreferences:SharedPreferences) */{

    private val sharedPreferences:SharedPreferences
    //region Default Values for various types
    //endregion
    //region Getters and Setters for default Values
    var intDefaultVal = 0
    var longDefaultVal:Long = 0
    var floatDefaultVal = 0f
    var isBoolDefaultVal = false
    var stringDefaultVal = ""
    var stringSetDefaultVal: Set<String>? = null
    //endregion
    //region Constructors
    /**
     * Uses the default shared preferences
     */
    constructor(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
    /**
     * creates a new preference file with specified name and mode
     * @param context
     * @param preferenceFileName
     * @param mode
     */
    @JvmOverloads constructor(context: Context, preferenceFileName: String, mode: Int = 0) {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, mode)
    }
    //endregion
    //#TODO Builder Pattern here can be helpful in making the use of this much elegant
    //region Put Methods
    fun putInt(key: String, `val`: Int) {
        sharedPreferences.edit().putInt(key, `val`).apply()
    }
    fun putString(key: String, `val`: String) {
        sharedPreferences.edit().putString(key, `val`).apply()
    }
    fun putBoolean(key: String, `val`: Boolean) {
        sharedPreferences.edit().putBoolean(key, `val`).apply()
    }
    fun putFloat(key: String, `val`: Float) {
        sharedPreferences.edit().putFloat(key, `val`).apply()
    }
    fun putLong(key: String, `val`: Long) {
        sharedPreferences.edit().putLong(key, `val`).apply()
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(key: String, `val`: Set<String>) {
        sharedPreferences.edit().putStringSet(key, `val`).apply()
    }
    //endregion
    //region Get Methods
    val all:Map<String, *>
        get() {
            return sharedPreferences.all
        }
    fun getInt(key: String, defaultValue: Int):Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
    fun getInt(key: String):Int {
        return sharedPreferences.getInt(key, intDefaultVal)
    }
    fun getString(key: String, defaultValue: String):String {
        return sharedPreferences.getString(key, defaultValue).toString()
    }
    fun getString(key: String):String {
        return sharedPreferences.getString(key, stringDefaultVal).toString()
    }
    fun getBoolean(key: String, defaultValue: Boolean):Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
    fun getBoolean(key: String):Boolean {
        return sharedPreferences.getBoolean(key, isBoolDefaultVal)
    }
    fun getFloat(key: String, defaultValue: Float):Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }
    fun getFloat(key: String):Float {
        return sharedPreferences.getFloat(key, floatDefaultVal)
    }
    fun getLong(key: String, defaultValue: Long):Long {
        return sharedPreferences.getLong(key, defaultValue)
    }
    fun getLong(key: String):Long {
        return sharedPreferences.getLong(key, longDefaultVal)
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String, defaultValue: Set<String>):Set<String> {
        return sharedPreferences.getStringSet(key, defaultValue) as Set<String>
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String):Set<String> {
        return sharedPreferences.getStringSet(key, stringSetDefaultVal) as Set<String>
    }
    //endregion
    //regiegion
    //region Listener registering and unregistering methods
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun storeArrayList(key: String, list: ArrayList<OrderCalculation>) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
        Log.e("fafa", json)
    }


    fun getArrayList(key: String): ArrayList<OrderCalculation> {

        val emptyList = Gson().toJson(ArrayList<OrderCalculation>())
        var json: String? =sharedPreferences.getString(key, emptyList)
        val arrayListTutorialType = object : TypeToken<ArrayList<OrderCalculation>>() {}.type
        var tutorials: ArrayList<OrderCalculation> = gson.fromJson(json, arrayListTutorialType)
        return tutorials
    }

    //endregion


    /*
        3. Using Gson
     */
   /* public List<String> getDataArrayList3() {
        String s = pref.getString(KEY_ARRAY_LIST3, null);
        List<String> list = new ArrayList<>();
        if (s != null) {
            Gson gson = new Gson();
            String[] arrString = gson.fromJson(s, String[].class);
            list = Arrays.asList(arrString);
        }
        return list;
    }*/

    /*fun getArrayList(key:String ):ArrayList<OrderCalculation>
    {
        var arrayList:ArrayList<OrderCalculation>
        arrayList=ArrayList()

        val emptyList = Gson().toJson(ArrayList<OrderCalculation>())
      //  var s : String? = sharedPreferences.getString(key, null)
        var s : String? =sharedPreferences.getString(key, emptyList)
        if (s != null) {
            val json = sharedPreferences.getString(key, emptyList)
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            arrayList= gson.fromJson(json, type)
        }
        return arrayList;
       // return emptyList
    }*/

    var welcome: Boolean
        get() = sharedPreferences.getBoolean("welcome", false)
        set(value) = sharedPreferences.edit().putBoolean("welcome", value).apply()

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean("isLogin", false)
        set(value) = sharedPreferences.edit().putBoolean("isLogin", value).apply()

    //return sharedPreferences.getString(key, defaultValue).toString()

    var fcmToken: String
        get() = sharedPreferences.getString("fcmToken", stringDefaultVal).toString()
        set(value) = sharedPreferences.edit().putString("fcmToken", value).apply()

    var isTokenSave: Boolean
        get() = sharedPreferences.getBoolean("isTokenSave", false)
        set(value) = sharedPreferences.edit().putBoolean("isTokenSave", value).apply()

    var allVehicleType: String
        get() = sharedPreferences.getString("allVehicleType", "[]")?:"[]"
        set(value) = sharedPreferences.edit().putString("allVehicleType", value).apply()
}