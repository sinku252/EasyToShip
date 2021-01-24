package com.tws.courier.ui.create_order

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.monrotv.ui.dashboard.adapters.DrawerAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.tws.courier.*
import com.tws.courier.R
import com.tws.courier.databinding.FragmentCreateOrderBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.base.BaseDrawerFragment
import com.tws.courier.domain.base.BaseFragment
import com.tws.courier.domain.models.OrderSuccess
import com.tws.courier.domain.models.OrderValidation
import com.tws.courier.ui.home.base.HomeBaseDrawerFragment
import com.tws.courier.ui.home.base.HomeBaseFragment
import kotlinx.android.synthetic.main.common_input_dialog.view.*

import kotlinx.android.synthetic.main.dimenstion_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*


class FragmentCreateOrder : HomeBaseDrawerFragment<CreateOrderViewModel, FragmentCreateOrderBinding>(),  OnMapReadyCallback
{
    companion object {
        val MY_PERMISSIONS_REQUEST_LOCATION = 99

        val TAG = "FragmentCreateOrder"
        fun newInstance() = FragmentCreateOrder()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_create_order
    override fun getViewModelClass(): Class<CreateOrderViewModel> = CreateOrderViewModel::class.java

    var googleMap: GoogleMap? = null
    // lateinit var placesAdapter: PlacesAdapter
    lateinit var latLng: LatLng
    lateinit var mLocationRequest: LocationRequest
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mLocationCallback: LocationCallback
    //  lateinit var mGeoDataClient: GeoDataClient
    lateinit var mSettingsClient: SettingsClient
    lateinit var mLocationSettingsRequest: LocationSettingsRequest
    private val REQUEST_CHECK_SETTINGS = 0x1
    var isAutoCompleteLocation = false
    lateinit var location: Location
    val REQUEST_LOCATION = 1011
    val BOUNDS_INDIA = LatLngBounds(LatLng(23.63936, 68.14712), LatLng(28.20453, 97.34466))

    var sheetBehavior: BottomSheetBehavior<*>? = null
    var orderValidation: OrderValidation = OrderValidation()
    var boolean:Boolean=false
    private val AUTOCOMPLETE_REQUEST_CODE = 10

    private val options = MarkerOptions()
    private val latlngs: ArrayList<LatLng> = ArrayList()

    lateinit var fromLatLng: LatLng
    lateinit var latLatLng: LatLng


    /* lateinit var startLatLng: LatLng
     lateinit var endLatLng: LatLng*/
    var openDrawer: MapsActivity.OpenDrawer? = null


    val drawerAdapter: DrawerAdapter = DrawerAdapter(
        DrawerAdapter.DrawerItem.getItemsList,
        object : DrawerAdapter.DrawerAdapterCallbacks {
            override fun onDrawerItemClicked(drawerItem: DrawerAdapter.DrawerItem) {
                closeDrawer()

                when (drawerItem.id) {
                    33 -> { // nothing yet
                        //viewModel.requestDashboardData()
                        //  fragmentListener?.navigateToDashboardFragment()
                    }
                    34 -> {
                        fragmentListener?.navigateToCreateShipment()
                    }
                    35 -> {
                        fragmentListener?.navigateToTokenFragment()
                    }
                    36 -> {
                        fragmentListener?.navigateToChatFragment()
                    }
                    37 -> {
                        fragmentListener?.navigateToDashboardHomeFragment()
                    }
                    38 -> {
                        fragmentListener?.navigateToProfileFragment()
                    }
                    39 -> {
                        fragmentListener?.navigateToNotificationFragment()
                    }

                    else -> showMessageDialog("Coming soon...")
                }
            }

            override fun onHeaderClicked() {
//                closeDrawer()
//                showMessageDialog("Coming soon...")
            }
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDrawer()
        var apiKey=getString(R.string.google_map_api_key)
        if(!Places.isInitialized())
        {
            activity?.let { Places.initialize(it, apiKey) }
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val loc = locationResult!!.lastLocation
                if (!isAutoCompleteLocation) {
                    location = loc
                    latLng = LatLng(location.latitude, location.longitude)
                    assignToMap()
                }
            }

        }

        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong())        // 10 seconds, in milliseconds
            .setFastestInterval((6 * 1000).toLong()) // 1 second, in milliseconds

        mSettingsClient = activity?.let { LocationServices.getSettingsClient(it) }!!
        val builder = LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()

        viewBinding.ivOpenDrawer.setOnClickListener {
            openCloseDrawer()
        }

        initView()
        observeLiveData()
    }

    private fun initView()
    {
        sheetBehavior = BottomSheetBehavior.from(viewBinding.bottomSheet.bottomSheet);
        if (sheetBehavior?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED);
        }


        //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)

        //    viewBinding.createOrder= createOrder
        //   createOrder.weight = "1"
        //  createOrder.vehicleType="bike"

        //  viewBinding.createOrder = createOrder
        viewBinding.orderValidation = orderValidation
        viewModel.allVehicleTypeList=mPreference?.getArrayList("allVehicleType")!!

        /*if(orderValidation.journeyType==1)
        {
            orderValidation.bikeValues
        }*/



//        AppDatePickerDialog.createAndShowDialog()
    }

    private fun observeLiveData()
    {
        viewModel.navigateToPlace.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //fragmentListener?.navigateToRegisterFragment()
                boolean = it
                findPlace()
            }
        })

        viewModel.selectDateTime.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //fragmentListener?.navigateToRegisterFragment()
                pickDateTime()
            }
        })



        viewModel.showBottomPopUp.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //fragmentListener?.navigateToRegisterFragment()
                if (sheetBehavior?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        })

        viewModel.radio_checked.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (orderValidation.journeyType == 1) {
                    if (viewBinding.rbBikeDocument.id == it) {
                        orderValidation.bikeValues.shipmentType = "document"
                    } else if (viewBinding.rbBikeParcel.id == it) {
                        orderValidation.bikeValues.shipmentType = "parcel"
                    }
                } else if (orderValidation.journeyType == 2) {
                    if (viewBinding.rbLocalParcel.id == it) {
                        orderValidation.localTruckValues.shipmentType = "parcel"
                    }
                    /* else if (viewBinding.rbDocument.id == it) {
                         orderValidation.localTruckValues.shipmentType = "parcel"
                     }*/
                } else if (orderValidation.journeyType == 3) {
                    if (orderValidation.domesticValues.domesticType.equals("domesticTruck")) {
                        if (viewBinding.rbDomesticParcel.id == it) {
                            orderValidation.domesticValues.domesticTruckValues?.shipmentType =
                                "parcel"
                        }
                        /* else if (viewBinding.rbDocument.id == it) {
                             orderValidation.domesticValues.domesticTruckValues?.shipmentType = "parcel"
                         }*/
                    } else if (orderValidation.domesticValues.domesticType.equals("domesticTrain")) {
                        if (viewBinding.rbDomesticParcel.id == it) {
                            orderValidation.domesticValues.domesticTrainValues?.shipmentType =
                                "parcel"
                        }
                        /*else if (viewBinding.rbDocument.id == it) {
                            orderValidation.domesticValues.domesticTrainValues?.shipmentType = "parcel"
                        }*/
                    } else if (orderValidation.domesticValues.domesticType.equals("domesticFlight")) {
                        if (viewBinding.rbDomesticParcel.id == it) {
                            orderValidation.domesticValues.domesticFlightValues?.shipmentType =
                                "parcel"
                        } else if (viewBinding.rbDomesticDocument.id == it) {
                            orderValidation.domesticValues.domesticFlightValues?.shipmentType =
                                "document"
                        }
                    }

                } else if (orderValidation.journeyType == 4) {
                    if (viewBinding.rbInternationalParcel.id == it) {
                        orderValidation.internationalValues.shipmentType = "parcel"
                    } else if (viewBinding.rbInternationalDocument.id == it) {
                        orderValidation.internationalValues.shipmentType = "document"
                    }
                }

                viewBinding.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            }

        })

        viewModel.oneWayTwoWay.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.bottomSheet.rbRoundTrip.id == it) {
                    orderValidation.localTruckValues.oneWayTwoWay = "roudtrip"
                } else if (viewBinding.bottomSheet.rbOneWay.id == it) {
                    orderValidation.localTruckValues.oneWayTwoWay = "oneway"
                }
                viewBinding.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            }
        })

        viewModel.domestic.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.bottomSheet.rbDomesticTruck.id == it) {
                    orderValidation.domesticValues.domesticType = "domesticTruck"
                } else if (viewBinding.bottomSheet.rbDomesticTrain.id == it) {
                    orderValidation.domesticValues.domesticType = "domesticTrain"
                } else if (viewBinding.bottomSheet.rbDomesticFlight.id == it) {
                    orderValidation.domesticValues.domesticType = "domesticFlight"
//                    viewBinding.viewmodel.isDocumentNeeded=true
                }
                //   viewBinding.viewmodel?.step = it
                viewBinding.orderValidation = orderValidation
                viewBinding.viewmodel = viewBinding?.viewmodel

            }
        })

        viewModel.importExport.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.bottomSheet.rbImport.id == it) {
                    orderValidation.internationalValues.importExport = "import"
                } else if (viewBinding.bottomSheet.rbExport.id == it) {
                    orderValidation.internationalValues.importExport = "export"
                }
            }
        })
        viewModel.personalCommercial.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (orderValidation.journeyType == 2) {
                    if (viewBinding.bottomSheet.rbPersonal.id == it) {
                        orderValidation.localTruckValues.personalOrCommercial = "personal"
                    } else if (viewBinding.bottomSheet.rbCommercial.id == it) {
                        orderValidation.localTruckValues.personalOrCommercial = "commercial"
                    }
                } else if (orderValidation.journeyType == 3) {
                    if (orderValidation.domesticValues.domesticType.equals("domesticFlight")) {
                        if (viewBinding.bottomSheet.rbPersonal.id == it) {
                            orderValidation.domesticValues.domesticFlightValues?.personalOrCommercial =
                                "personal"
                        } else if (viewBinding.bottomSheet.rbCommercial.id == it) {
                            orderValidation.domesticValues.domesticFlightValues?.personalOrCommercial =
                                "commercial"
                        }
                    } else if (orderValidation.domesticValues.domesticType.equals("domesticTrain")) {
                        if (viewBinding.bottomSheet.rbPersonal.id == it) {
                            orderValidation.domesticValues.domesticTrainValues?.personalOrCommercial =
                                "personal"
                        } else if (viewBinding.bottomSheet.rbCommercial.id == it) {
                            orderValidation.domesticValues.domesticTrainValues?.personalOrCommercial =
                                "commercial"
                        }
                    } else if (orderValidation.domesticValues.domesticType.equals("domesticTruck")) {
                        if (viewBinding.bottomSheet.rbPersonal.id == it) {
                            orderValidation.domesticValues.domesticTruckValues?.personalOrCommercial =
                                "personal"
                        } else if (viewBinding.bottomSheet.rbCommercial.id == it) {
                            orderValidation.domesticValues.domesticTruckValues?.personalOrCommercial =
                                "commercial"
                        }
                    }
                } else if (orderValidation.journeyType == 4) {
                    if (viewBinding.bottomSheet.rbPersonal.id == it) {
                        orderValidation.internationalValues.personalOrCommercial = "personal"
                    } else if (viewBinding.bottomSheet.rbCommercial.id == it) {
                        orderValidation.internationalValues.personalOrCommercial = "commercial"
                    }
                }

                viewBinding?.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            }
        })

        viewModel.vehicleType.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                /*if (it == viewBinding.bottomSheet.rbBike.id) {
                    createOrder.vehicleType = "bike"
                } else if (it == viewBinding.bottomSheet.rbTruck.id) {
                    createOrder.vehicleType = "truck"
                } else if (it == viewBinding.bottomSheet.rbDomestic.id) {
                    createOrder.vehicleType = "national"
                } else if (it == viewBinding.bottomSheet.rbInternational.id) {
                    createOrder.vehicleType = "international"
                }*/
                /*if (viewBinding.bottomSheet.rbBike.id == it) {
                    createOrder.import = "1"
                }*/
                viewBinding.orderValidation = orderValidation
                //viewBinding.createOrder = createOrder
            }

        })

        viewModel.showViewByType.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.type == 0)
                    viewModel.checkValidation(viewBinding.orderValidation!!)
                else if (it.type == 1)
                    viewModel.checkStep2Validation(viewBinding.orderValidation!!)


            }
        })

        viewModel.showViewByStep.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //fragmentListener?.navigateToRegisterFragment()
                viewBinding.viewmodel?.step = it
                viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
            }
        })

        viewModel.showInputDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it == 1)
                    showInputBox(it, "Pickup Mobile Number", "Mobile Number")
                else if (it == 2)
                    showInputBox(it, "Delivery Mobile Number", "Mobile Number")
                else if (it == 3)
                    showInputBox(it, "Weight", "Enter Weight")
                else if (it == 4)
                    showInputBox(it, "Porter", "Enter Number of Porter")
                else if (it == 5)
                    showDimentionBox(it, "Dimension", "Enter Number of Porter")
            }
        })

        viewModel.onCheckBoxSelect.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //fragmentListener?.navigateToRegisterFragment()
                if (it.position == 1) {
                    if (orderValidation.journeyType == 4) {
                        if (it.checked)
                            orderValidation.internationalValues.insurance = "1"
                        else
                            orderValidation.internationalValues.insurance = "0"
                    } else if (orderValidation.journeyType == 3) {
                        if (orderValidation.domesticValues.domesticType.equals("domesticFlight")) {
                            if (it.checked)
                                orderValidation.domesticValues.domesticFlightValues?.insurance = "1"
                            else
                                orderValidation.domesticValues.domesticFlightValues?.insurance = "0"
                        } else if (orderValidation.domesticValues.domesticType.equals("domesticTrain")) {
                            if (it.checked)
                                orderValidation.domesticValues.domesticTrainValues?.insurance = "1"
                            else
                                orderValidation.domesticValues.domesticTrainValues?.insurance = "0"
                        } else if (orderValidation.domesticValues.domesticType.equals("domesticTruck")) {
                            if (it.checked)
                                orderValidation.domesticValues.domesticTruckValues?.insurance = "1"
                            else
                                orderValidation.domesticValues.domesticTruckValues?.insurance = "0"
                        }
                    }
                } else if (it.position == 2) {
                    // viewBinding.bottomSheet.llPorter.visibility=View.VISIBLE
                    if (it.checked)
                        showInputBox(4, "Porter", "Enter Number of Porter")
                    /*  if (it.checked)
                        viewBinding.bottomSheet.llPorter.visibleOrGone = true
                    else
                        viewBinding.bottomSheet.llPorter.visibleOrGone = false*/
                } else if (it.position == 3) {
                    if (orderValidation.domesticValues.domesticType.equals("domesticTrain")) {
                        if (it.checked)
                            orderValidation.domesticValues.domesticTrainValues?.dc = "1"
                        else
                            orderValidation.domesticValues.domesticTrainValues?.dc = "0"
                    } else if (orderValidation.domesticValues.domesticType.equals("domesticTruck")) {
                        if (it.checked)
                            orderValidation.domesticValues.domesticTruckValues?.dc = "1"
                        else
                            orderValidation.domesticValues.domesticTruckValues?.dc = "0"
                    }
                }
                viewBinding?.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            }
        })

        viewModel.journeyType.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //fragmentListener?.navigateToRegisterFragment()
                if (it == 0) {
                    orderValidation.journeyType = 1
                    viewBinding.bottomSheet.ivBike.setImageResource(R.drawable.bike_new_svg_green);
                    viewBinding.bottomSheet.ivTruck.setImageResource(R.drawable.truck_new_svg_yellow);
                    viewBinding.bottomSheet.ivDomestic.setImageResource(R.drawable.domestic_new_svg_yellow);
                    viewBinding.bottomSheet.ivInternational.setImageResource(R.drawable.international_new_svg_yellow);

                } else if (it == 1) {
                    orderValidation.journeyType = 2
                    viewBinding.bottomSheet.ivBike.setImageResource(R.drawable.bike_new_svg_yellow);
                    viewBinding.bottomSheet.ivTruck.setImageResource(R.drawable.truck_new_svg_green);
                    viewBinding.bottomSheet.ivDomestic.setImageResource(R.drawable.domestic_new_svg_yellow);
                    viewBinding.bottomSheet.ivInternational.setImageResource(R.drawable.international_new_svg_yellow);
                } else if (it == 2) {
                    orderValidation.journeyType = 3
                    viewBinding.bottomSheet.ivBike.setImageResource(R.drawable.bike_new_svg_yellow);
                    viewBinding.bottomSheet.ivTruck.setImageResource(R.drawable.truck_new_svg_yellow);
                    viewBinding.bottomSheet.ivDomestic.setImageResource(R.drawable.domestic_new_svg_green);
                    viewBinding.bottomSheet.ivInternational.setImageResource(R.drawable.international_new_svg_yellow);
                } else if (it == 3) {
                    orderValidation.journeyType = 4
                    viewBinding.bottomSheet.ivBike.setImageResource(R.drawable.bike_new_svg_yellow);
                    viewBinding.bottomSheet.ivTruck.setImageResource(R.drawable.truck_new_svg_yellow);
                    viewBinding.bottomSheet.ivDomestic.setImageResource(R.drawable.domestic_new_svg_yellow);
                    viewBinding.bottomSheet.ivInternational.setImageResource(R.drawable.international_new_svg_green);
                }



                viewBinding?.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation

            }
        })

        viewModel.transmissionSpeed.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.bottomSheet.rbSilver.id == it) {
                    orderValidation.transmissionSpeed = "silver"
                } else if (viewBinding.bottomSheet.rbGold.id == it) {
                    orderValidation.transmissionSpeed = "gold"
                } else if (viewBinding.bottomSheet.rbPlatinum.id == it) {
                    orderValidation.transmissionSpeed = "platinum"
                }
                viewBinding?.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            }
        })

        viewModel.paymentMode.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.bottomSheet.rbCop.id == it) {
                    orderValidation.paymentMode = "cop"
                } else if (viewBinding.bottomSheet.rbCod.id == it) {
                    orderValidation.paymentMode = "cod"
                } else if (viewBinding.bottomSheet.rbWallet.id == it) {
                    orderValidation.paymentMode = "wallet"
                }

                viewBinding?.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            }
        })

        viewModel.onOrderPriceSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                viewBinding.orderPrice = it
                viewBinding.bottomSheet.orderPrice = viewBinding?.orderPrice
            }
        })

        viewModel.onOrderSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                showToastShort = "Orders successfully"
                //if(it.paymentMode.equals("online"))
                //finish()
                //   fragmentListener?.navigateToDashboardFragment()
                //fragmentListener?.navigateToBookingSuccessFragment(it)
                val gson = Gson()
                val json: String = gson.toJson(it)
                /*val intent = Intent()
                intent.putExtra("OrderSuccess", json)
                setResult(Activity.RESULT_OK, intent)
                finish()*/

                var orderSuccess: OrderSuccess = gson.fromJson(json, OrderSuccess::class.java)
                fragmentListener?.navigateToBookingSuccessFragment(orderSuccess)
            }
        })

        viewModel.navigateToInsurance.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
               fragmentListener?.navigateToInsurance()
            }
        })
    }





    private fun assignToMap() {
        googleMap?.clear()

        val options = MarkerOptions()
            .position(latLng)
            .title("Current Location")

        googleMap?.apply {
            addMarker(options)
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
        for (point in latlngs) {
            options.position(point)
            if (boolean)
                options.title(viewBinding.tvFrom.text.toString())
            else
                options.title(viewBinding.tvTo.text.toString())
            //  googleMap!!.addMarker(options)
            googleMap?.apply {
                //addMarker(options)
                moveCamera(CameraUpdateFactory.newLatLng(point))
                if (boolean)
                animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10f))
                else
                    animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
            }
        }

    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()?.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    location = task.getResult()
                    latLng = LatLng(location.latitude, location.longitude)
                    assignToMap()

                } else {
                    Log.w("Location", "Failed to get location.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e("Location", "Lost location permission." + unlikely)
        }

    }

    private fun initLocation() {
        try {
            mFusedLocationClient = activity?.let {
                LocationServices.getFusedLocationProviderClient(it)
            }!!
            getLastLocation()
            try {

                mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(requireActivity(), object :
                        OnSuccessListener<LocationSettingsResponse> {
                        override fun onSuccess(p0: LocationSettingsResponse?) {
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        }

                    }).addOnFailureListener(requireActivity(), object : OnFailureListener {
                        override fun onFailure(p0: java.lang.Exception) {
                            val statusCode = (p0 as ApiException).getStatusCode();
                            when (statusCode) {
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                    Log.i("Location",
                                        "Location settings are not satisfied. Attempting to upgrade " +
                                                "location settings ");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        val rae = p0 as ResolvableApiException
                                        rae.startResolutionForResult(activity,
                                            REQUEST_CHECK_SETTINGS);
                                    } catch (sie: IntentSender.SendIntentException) {
                                        Log.i("Location",
                                            "PendingIntent unable to execute request.");
                                    }
                                }

                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                                    Toast.makeText(activity,
                                        "Location settings are inadequate, and cannot be \"+\n" +
                                                "                                    \"fixed here. Fix in Settings.",
                                        Toast.LENGTH_LONG).show();


                            }
                        }

                    })

            } catch (unlikely: SecurityException) {
                Log.e("Location",
                    "Lost location permission. Could not request updates. " + unlikely)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onMapReady(p0: GoogleMap?) {
        Log.v("googleMap", "googleMap==" + googleMap)
        googleMap = p0
        googleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        googleMap?.getUiSettings()?.apply {
            isZoomControlsEnabled = false
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }

        googleMap!!.setOnMapClickListener { arg0 -> // TODO Auto-generated method stub
            if (sheetBehavior?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation()
            } else {
                Toast.makeText(activity, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = activity?.let {
            ActivityCompat.checkSelfPermission(it,
                Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }

    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            initLocation()
        } else {
            requestPermissions();
        }
    }

    fun findPlace()
    {


        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        //val fields = listOf(Place.Field.ID, Place.Field.NAME)
        val fields = Arrays.asList(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS)

        // Start the autocomplete intent.
        val intent =
            activity?.let {
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(it)
            }
        /* activity?.let {
             Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                 .build(it)
         }*/
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun pickDateTime() {



        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        val dpd = activity?.let {
            DatePickerDialog(it,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val tpd = TimePickerDialog(activity,
                        TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                            val pickedDateTime = Calendar.getInstance()
                            pickedDateTime.set(year, month, day, hour, minute)
                            //val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
                            // doSomethingWith(pickedDateTime)
                            /* val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm",
                            Locale.ENGLISH
                        )
                        val df: DateFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm aaa" , Locale.ENGLISH)
                        val date: Date = sdf.parse(pickedDateTime.time.toString())*/ // converting String to date
                            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                            val strDate = formatter.format(pickedDateTime.time)
                            viewBinding.viewmodel?.formatedDate = strDate
                            viewBinding.viewmodel?.selectedDate = getDateWithMonthNameFromCalendar(
                                pickedDateTime.time.toString())
                            viewBinding.viewmodel = viewBinding.viewmodel
                            viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
                            //viewBinding.bottomSheet.tvDateTime.setText(getDateWithMonthNameFromCalendar(pickedDateTime.time.toString()))
                        },
                        startHour,
                        startMinute,
                        false).show()
                },

                startYear,
                startMonth,
                startDay)
        }
        dpd?.datePicker?.minDate=System.currentTimeMillis()-1000
        dpd?.show()



    }
    fun showInputBox(type: Int, title: String, hint: String){

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.common_input_dialog, null)
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }

        if(type==4)
            messageBoxBuilder?.setCancelable(false)

        messageBoxView.et_input.hint=hint
        messageBoxView.common_dialog_title.text=title

        val  messageBoxInstance = messageBoxBuilder?.show()


        //set Listener
        messageBoxView.common_dialog_ok.setOnClickListener(){
            //close dialog
            /*fragmentListener?.navigateToPaymentActivity("100")
            messageBoxInstance?.dismiss()*/
            if(type==1)
                viewBinding.viewmodel?.pickupMobileNumber=messageBoxView.et_input.text.toString()
            else if(type==2)
                viewBinding.viewmodel?.deliveryMobileNumber=messageBoxView.et_input.text.toString()
            else if(type==3)
                viewBinding.viewmodel?.weight=messageBoxView.et_input.text.toString()
            else if(type==4)
                if(!TextUtils.isEmpty(messageBoxView.et_input.text.toString().trim()))
                    viewBinding.viewmodel?.porterValue=messageBoxView.et_input.text.toString()
                else
                    viewBinding.bottomSheet.cbPorter.isChecked=false


            viewBinding.viewmodel=viewBinding.viewmodel
            viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
            messageBoxInstance?.dismiss()
        }
    }
    fun showDimentionBox(type: Int, title: String, hint: String){

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.dimenstion_dialog, null)
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }

        //messageBoxView.et_input.hint=hint
        messageBoxView.text_title.text=title

        val  messageBoxInstance = messageBoxBuilder?.show()

        //set Listener
        messageBoxView.button_ok.setOnClickListener()
        {
            if (TextUtils.isEmpty(messageBoxView.et_item_height.text.trim()))
            {
                showToastShort= AppManager.getString(R.string.err_height_empty)
            }
            else if (TextUtils.isEmpty(messageBoxView.et_item_width.text.trim()))
            {
                showToastShort= AppManager.getString(R.string.err_width_empty)
            }
            else if (TextUtils.isEmpty(messageBoxView.et_item_length.text.trim()))
            {
                showToastShort= AppManager.getString(R.string.err_length_empty)
            }
            else
            {

                var height:String=messageBoxView.et_item_height.text.toString()
                var width:String=messageBoxView.et_item_width.text.toString()
                var length:String=messageBoxView.et_item_length.text.toString()

                viewBinding.viewmodel?.dimension=height+"x"+width+"x"+length
                viewBinding.viewmodel=viewBinding.viewmodel
                viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
                messageBoxInstance?.dismiss()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(it)
                        if (place.getLatLng() != null) {
                            if (boolean) {
                                viewBinding.tvFrom.setText(place.name)
                                orderValidation.origin = place.name.toString()
                                orderValidation.startLatLng = place.latLng!!
                                // latlngs: ArrayList<LatLng> = ArrayList()
                            } else {
                                viewBinding.tvTo.setText(place.name)
                                orderValidation.destination = place.name.toString()
                                orderValidation.endLatLng = place.latLng!!
                            }


                            viewBinding.orderValidation = orderValidation
                            viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation

                            Log.i(FragmentCreateOrder.TAG,
                                "Place: ${place.name}, ${place.id},${place.latLng}")
                            isAutoCompleteLocation = true
                            latLng = place.latLng!!
                            latlngs.add(place.latLng!!)

                            assignToMap()
                        }


                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(FragmentCreateOrder.TAG, status.statusMessage)
                        // viewModel.showBottomPopUp.value = SingleActionEvent(true)
                        //viewBinding.tvFrom.setText("sdfksd f")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showInputError(inputError: BaseFragment.InputError) {
        when (inputError.errorType) {
            InputErrorType.MESSAGE -> {
                showMessageDialog(inputError.message)
            }
        }
    }

    private fun setupDrawer() {
        // Calling the magical method will do the rest
        initDrawer(object : BaseDrawerFragment.DrawerCreationInfo {
            override val drawerRecyclerView: RecyclerView = viewBinding.recyclerDrawer
            override var drawerRecyclerLayoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(context)
            override val drawerRecyclerAdapter: RecyclerView.Adapter<*> = drawerAdapter
            override val drawerGravity: Int = GravityCompat.START
            override val drawerOpenContentDesc: Int = R.string.drawer_close
            override val drawerCloseContentDesc: Int = R.string.drawer_close
            override val drawerLayout: DrawerLayout = viewBinding.drawerLayout
        })

        setToolbarBackButtonIcon(R.drawable.nav)
        overrideBackButtonClickListener(View.OnClickListener {
            openCloseDrawer()
        })
    }



}