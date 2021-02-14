package com.tws.courier.ui.create_order

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.monrotv.ui.dashboard.adapters.DrawerAdapter
import com.google.android.gms.common.api.ApiException
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
import com.tws.courier.domain.base.SingleActionEvent
import com.tws.courier.domain.models.*
import com.tws.courier.domain.utils.Utils
import com.tws.courier.ui.home.base.HomeBaseDrawerFragment
import kotlinx.android.synthetic.main.common_input_dialog.view.*
import kotlinx.android.synthetic.main.common_input_dialog.view.common_dialog_title
import kotlinx.android.synthetic.main.dimenstion_dialog.view.*
import kotlinx.android.synthetic.main.dimenstion_dialog.view.button_ok
import kotlinx.android.synthetic.main.dimenstion_dialog.view.text_title
import kotlinx.android.synthetic.main.payment_dialog.view.*
import kotlinx.android.synthetic.main.price_dialog.view.*
import kotlinx.android.synthetic.main.weight_input_dialog.view.*
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
                        fragmentListener?.navigateToDashboardFragment()
                    }
                    34 -> {
                        //fragmentListener?.navigateToCreateShipment()
                        fragmentListener?.navigateToDashboardFragment()
                    }
                    35 -> {
                        fragmentListener?.navigateToDashboardHomeFragment()
                    }
                    36 -> {
                        fragmentListener?.navigateToAccountSetting()
                    }
                    37 -> {
                        fragmentListener?.navigateToWallet()
                    }
                    38 -> {
                        fragmentListener?.navigateToProfileFragment()
                    }
                    39 -> {
                        fragmentListener?.navigateToChatFragment()
                    }
                    40 -> {
                        //shareApp(activity)
                        fragmentListener?.navigateToTokenFragment()
                        // fragmentListener?.navigateToNotificationFragment()
                    }
                    41 -> {
                        Utils.shareApp(activity)
                    }
                    42 -> {
                        fragmentListener?.navigateToNotificationFragment()
                    }
                    43 -> {
                        //contact us
                    }
                    44 -> {
                        showMessageDialog(getString(R.string.app_name),
                            getString(R.string.warn_logout),
                            View.OnClickListener {
                                mPreference?.isLogin = false
                                fragmentListener?.logout()
                            },
                            View.OnClickListener { })
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
                    orderValidation.startLatLng=latLng
                    viewBinding.tvFrom.setText(getCityName(latLng))
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
        println(orderValidation)

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

                /*    orderValidation.domesticValues.domesticTruckValues?.shipmentType=""
                orderValidation.domesticValues.domesticTrainValues?.shipmentType=""
                orderValidation.domesticValues.domesticFlightValues?.shipmentType=""*/

                viewModel.radio_checked.postValue(viewModel.radio_checked.value)
                viewBinding.orderValidation = orderValidation
                //  viewBinding.orderValidation =  viewBinding?.orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation

                //viewBinding.viewmodel?.radio_checked?.postValue(viewBinding.radio_checked)

            }
        })

        viewModel.importExport.observe(viewLifecycleOwner, Observer {
            if (it != 1) {
                if (viewBinding.bottomSheet.rbImport.id == it) {
                    orderValidation.internationalValues.importExport = "import"
                } else if (viewBinding.bottomSheet.rbExport.id == it) {
                    orderValidation.internationalValues.importExport = "export"
                }
                viewBinding.orderValidation = orderValidation
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
                } else {
                    var value = ""
                    if (viewBinding.bottomSheet.rbPersonal.id == it) {
                        value = "personal"
                    } else if (viewBinding.bottomSheet.rbCommercial.id == it) {
                        value = "commercial"
                    }

                    orderValidation.localTruckValues.personalOrCommercial = value
                    orderValidation.domesticValues.domesticTruckValues?.personalOrCommercial = value
                    orderValidation.domesticValues.domesticTrainValues?.personalOrCommercial = value
                    orderValidation.domesticValues.domesticFlightValues?.personalOrCommercial =
                        value
                    orderValidation.internationalValues.personalOrCommercial = value
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

        viewModel.showContactList.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(intent, it)
            }
        })

        viewModel.showInputDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it == 1)
                    showInputBox(it, "Pickup Mobile Number", "Mobile Number")
                else if (it == 2)
                    showInputBox(it, "Delivery Mobile Number", "Mobile Number")
                else if (it == 3)
                    //showWeightBox()
                    showInputBox(it, "Weight(kg)", "Enter Weight")
                else if (it == 4)
                    showInputBox(it, "Porter", "Enter Number of Porter")
                else if (it == 5)
                    showDimentionBox(it, "Dimensions (cm)", "Enter Number of Porter")
                else if (it == 6)
                    showPaymentBox(it, "Payment", "Select Payment")
                else if (it == 7)
                    showPriceBox(viewBinding.orderPrice!!)
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

                if (viewModel.step == 2)
                    view?.let { it1 -> viewModel.btnClick(it1, 1) }

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
            if (it > 0) {
                Log.e("gdgsdg", "" + it)
                var orderPrice: OrderPrice? = viewBinding?.orderPrice
                var totalWeightPriceList: List<TotalWeightPrice>
                if (orderValidation.journeyType == 3)
                    totalWeightPriceList = orderPrice?.totalWeightPriceNational!!
                else
                    totalWeightPriceList = orderPrice?.totalWeightPriceInternational!!

                if (totalWeightPriceList.size > 3) {
                    if (it == 100) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(0).type
                       /* viewModel.orderPriceCalculted = getOrderPriceByRadio(0,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    } else if (it == 101) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(1).type
                        /*viewModel.orderPriceCalculted = getOrderPriceByRadio(1,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    } else if (it == 102) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(2).type
                      /*  viewModel.orderPriceCalculted = getOrderPriceByRadio(2,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    } else if (it == 103) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(3).type
                        /*viewModel.orderPriceCalculted = getOrderPriceByRadio(3,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    } else if (it == 104) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(4).type
                      /*  viewModel.orderPriceCalculted = getOrderPriceByRadio(4,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    }
                } else {
                    if (it == 100) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(0).type
                      /*  viewModel.orderPriceCalculted = getOrderPriceByRadio(0,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    } else if (it == 101) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(1).type
                      /*  viewModel.orderPriceCalculted = getOrderPriceByRadio(1,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    } else if (it == 102) {
                        orderValidation.transmissionSpeed = totalWeightPriceList.get(2).type
                        /*viewModel.orderPriceCalculted = getOrderPriceByRadio(2,
                            orderPrice,
                            orderValidation)*/
                        if (viewModel.step == 2)
                            view?.let { it1 -> viewModel.btnClick(it1, 1) }
                    }
                }



                viewBinding?.orderValidation = orderValidation
                viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
                viewBinding.bottomSheet.viewmodel = viewModel



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


              //  viewModel.orderPriceCalculted = getOrderPriceByType(orderValidation, it)

                viewBinding.orderPrice = it
                viewBinding.bottomSheet.orderPrice = viewBinding?.orderPrice
                viewBinding.bottomSheet.viewmodel = viewModel


                if (orderValidation.domesticValues.domesticType.equals("domesticFlight")) {
                    if(viewBinding.bottomSheet.rg.childCount==0)
                    {
                        var radioGroup: RadioGroup = createRadioButton(viewBinding.bottomSheet.rg, it)
                        viewModel.transmissionSpeed.postValue(radioGroup.getChildAt(0).id)
                    }
                } else if (orderValidation.journeyType == 4) {
                    if(viewBinding.bottomSheet.rgInternational.childCount==0)
                    {
                        var radioGroup: RadioGroup =
                            createRadioButton(viewBinding.bottomSheet.rgInternational,
                                it)
                        viewModel.transmissionSpeed.postValue(radioGroup.getChildAt(0).id)
                    }
                }

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

    fun showPriceBox(orderPrice: OrderPrice) {

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.price_dialog,
            null)
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }
        val  messageBoxInstance = messageBoxBuilder?.show()
        messageBoxView.tv_order_fair.text=orderPrice.totalFair
        messageBoxView.tv_order_insurance.text=orderPrice.totalInsuranceCharge
        messageBoxView.tv_order_porter.text=orderPrice.totalPorterCharge
        messageBoxView.tv_order_reverse.text=orderPrice.totalReverseTrip
        messageBoxView.tv_order_delivery_challan.text=orderPrice.totalDeliveryChallan
        messageBoxView.tv_order_gst.text=orderPrice.totalGstCharge
        messageBoxView.tv_order_peak.text=orderPrice.totalPeakCharge
        messageBoxView.tv_order_total.text=getString(R.string.rs, orderPrice.totalAmountPrice)

        messageBoxView.price_dialog_ok.setOnClickListener(){
            messageBoxInstance?.dismiss()
        }

    }

    fun showWeightBox() {

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.weight_input_dialog,
            null)
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }
        val  messageBoxInstance = messageBoxBuilder?.show()

        //var type=orderValidation.journeyType
        var weight:String="1"
        if(orderValidation.journeyType==3)
        {
            if(orderValidation.domesticValues.domesticType.equals("domesticFlight"))
            {
                if(orderValidation.domesticValues.domesticFlightValues?.equals("document")!!)
                {
                    weight="1"
                }
                else if(orderValidation.domesticValues.domesticFlightValues?.equals("parcel")!!)
                {
                    weight="100"
                }
            }
        }
        else if(orderValidation.journeyType==4)
        {
            if(orderValidation.internationalValues.equals("document"))
            {
                weight="1"
            }
            else if(orderValidation.internationalValues.equals("parcel"))
            {
                weight="100"
            }
        }


        messageBoxView.weight.setTextOrEmpty(weight)


        messageBoxView.decrement.setOnClickListener()
        {

            messageBoxView.weight.setTextOrEmpty(changeWeight(weight,messageBoxView.weight.text.toString(),2).toString())

        }

        messageBoxView.increment.setOnClickListener()
        {
            messageBoxView.weight.setTextOrEmpty(changeWeight(weight,messageBoxView.weight.text.toString(),1).toString())
        }


        messageBoxView.weight_ok.setOnClickListener(){
            viewBinding.viewmodel?.weight=messageBoxView.et_input.text.toString()
            viewBinding.viewmodel=viewBinding.viewmodel
            viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
            messageBoxInstance?.dismiss()
        }

    }


    fun showInputBox(type: Int, title: String, hint: String){

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.common_input_dialog,
            null)
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
            {
                if (TextUtils.isEmpty(messageBoxView.et_input.text.trim())) {
                    showToastShort= AppManager.getString(R.string.err_phone_empty)
                }
                else if (messageBoxView.et_input.text.length!=10)
                {
                    showToastShort= AppManager.getString(R.string.err_mobile_min_chars)
                }
                else if(!Utils.isValidPhoneNumber(messageBoxView.et_input.text.trim().toString())) {
                    showToastShort= AppManager.getString(R.string.err_mobile_min_chars)
                }
                else
                {
                    viewBinding.viewmodel?.pickupMobileNumber=messageBoxView.et_input.text.toString()
                }
            }
            else if(type==2)
            {
                if (TextUtils.isEmpty(messageBoxView.et_input.text.trim())) {
                    showToastShort= AppManager.getString(R.string.err_phone_empty)
                }
                else if (messageBoxView.et_input.text.length!=10)
                {
                    showToastShort= AppManager.getString(R.string.err_mobile_min_chars)
                }
                else if(!Utils.isValidPhoneNumber(messageBoxView.et_input.text.toString())) {
                    showToastShort= AppManager.getString(R.string.err_mobile_min_chars)
                }
                else
                {
                    viewBinding.viewmodel?.deliveryMobileNumber=messageBoxView.et_input.text.toString()
                }
            }
            else if(type==3)
            {
                if (TextUtils.isEmpty(messageBoxView.et_input.text.trim())) {
                    showToastShort= AppManager.getString(R.string.err_weight_empty)
                }
                else
                {
                    viewBinding.viewmodel?.weight=messageBoxView.et_input.text.toString()
                }
            }
            else if(type==4)
                if(!TextUtils.isEmpty(messageBoxView.et_input.text.toString().trim()))
                    viewBinding.viewmodel?.porterValue=messageBoxView.et_input.text.toString()
                else
                    viewBinding.bottomSheet.cbPorter.isChecked=false


            if(viewModel.step==2)
                viewModel.btnClick(it, 1)


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

                if(viewModel.step==2)
                viewModel.btnClick(it, 1)

                viewBinding.viewmodel?.dimension=height+"x"+width+"x"+length
                viewBinding.viewmodel=viewBinding.viewmodel
                viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
                messageBoxInstance?.dismiss()
            }

        }
    }

    fun showPaymentBox(type: Int, title: String, hint: String){

        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.payment_dialog, null)
        val messageBoxBuilder = activity?.let { AlertDialog.Builder(it).setView(messageBoxView) }

        //messageBoxView.et_input.hint=hint
        messageBoxView.text_title.text=title

        val  messageBoxInstance = messageBoxBuilder?.show()

        //set Listener
        messageBoxView.button_ok.setOnClickListener()
        {


            val selectedId: Int = messageBoxView.rb_payment.getCheckedRadioButtonId()
            var radioButton = messageBoxView.findViewById(selectedId) as RadioButton

            var paymentMode:String=""


            if (radioButton.id==R.id.rb_cod)
            {
                paymentMode="Cash On Delivery (cod)"
                orderValidation.paymentMode="cod"
            }
            else if (radioButton.id==R.id.rb_cop)
            {
                paymentMode="Cash On Pickup (cop)"
                orderValidation.paymentMode="cop"
            }
            else if (radioButton.id==R.id.rb_wallet)
            {
                paymentMode="wallet"
                orderValidation.paymentMode="wallet"
            }

            viewBinding.orderValidation=orderValidation
            viewBinding.viewmodel?.payment=paymentMode
            viewBinding.viewmodel=viewBinding.viewmodel
            viewBinding.orderValidation=viewBinding.orderValidation
            viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
            viewBinding.bottomSheet.orderValidation = viewBinding?.orderValidation
            messageBoxInstance?.dismiss()



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
        else if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Utils.shareApp(activity)
                    data?.let{
                        activity?.let{
                            viewBinding.viewmodel?.pickupMobileNumber =Utils.contactPicked(activity,data)
                            viewBinding.viewmodel = viewBinding.viewmodel
                            viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
                    }

                    }

                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
        else if (requestCode == 2) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    viewBinding.viewmodel?.deliveryMobileNumber =Utils.contactPicked(activity,data);
                    viewBinding.viewmodel = viewBinding.viewmodel
                    viewBinding.bottomSheet.viewmodel = viewBinding?.viewmodel
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
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



    private fun createRadioButton(rg: RadioGroup, orderPrice: OrderPrice) :RadioGroup{
        var totalWeightPriceList: List<TotalWeightPrice>
        if(rg.id==R.id.rg)
            totalWeightPriceList= orderPrice.totalWeightPriceNational!!
        else
            totalWeightPriceList= orderPrice.totalWeightPriceInternational!!
        rg.removeAllViews()
        for (i in 0 until totalWeightPriceList.size) {
            if(totalWeightPriceList.get(i).value!=null && !totalWeightPriceList.get(i).value.equals(
                    "0"))
            {
                val radioButton = RadioButton(activity)
                radioButton.text = totalWeightPriceList.get(i).type.toUpperCase()
                radioButton.id = 100+i
                radioButton.setPadding(10, 10, 10, 10)

                if(Build.VERSION.SDK_INT>=21)
                    radioButton.buttonTintList=activity?.getResources()?.getColorStateList(R.color.yellow_color)
                rg.addView(radioButton)
            }
        }
        return  rg

    }


    fun getOrderPriceByType(orderValidation: OrderValidation, orderPrice: OrderPrice):String
    {
        var price:String=""
        if(orderValidation.journeyType==3 && orderValidation.domesticValues.domesticType.equals("domesticFlight"))
            price= orderPrice.totalWeightPriceNational!!.get(0).value
        else if(orderValidation.journeyType==4)
        {
            if(orderPrice.totalWeightPriceInternational!!.get(0).value!=null && !orderPrice.totalWeightPriceInternational!!.get(
                    0).value.equals("0"))
            {
                price= orderPrice.totalWeightPriceInternational!!.get(0).value
            }
            else
            {
                price= orderPrice.totalWeightPriceInternational!!.get(1).value
            }
        }

        else
            price= orderPrice.totalAmountPrice

        return price
    }

    fun getOrderPriceByRadio(
        position: Int,
        orderPrice: OrderPrice,
        orderValidation: OrderValidation
    ):String
    {
        var price:String=""
        if(orderValidation.journeyType==3 && orderValidation.domesticValues.domesticType.equals("domesticFlight"))
            price= orderPrice.totalWeightPriceNational!!.get(position).value
        else if(orderValidation.journeyType==4)
            price= orderPrice.totalWeightPriceInternational!!.get(position).value

        return price
    }



    fun getCityName(latLng: LatLng):String
    {
        val geocoder = Geocoder(activity)
        val list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return list[0].locality
       /* val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(MyLat, MyLong, 1)
        val cityName: String = addresses[0].getAddressLine(0)
        val stateName: String = addresses[0].getAddressLine(1)
        val countryName: String = addresses[0].getAddressLine(2)
        return cityName*/
    }

    fun changeWeight(increment:String,weight:String, type: Int):Int
    {
        var maxWeight:Int=0
        if(orderValidation.journeyType==1)
        {
            if(orderValidation.bikeValues.shipmentType.equals("document"))
            {
                maxWeight=10
            }
            else if(orderValidation.bikeValues.shipmentType.equals("parcel"))
            {
                maxWeight=15
            }
        }
        else if(orderValidation.journeyType==3)
        {
            if(orderValidation.domesticValues.domesticType.equals("domesticFlight"))
            {
                if(orderValidation.domesticValues.domesticFlightValues?.equals("document")!!)
                {
                    maxWeight=5
                }
                else if(orderValidation.domesticValues.domesticFlightValues?.equals("parcel")!!)
                {
                    maxWeight=1000
                }
            }
        }
        else if(orderValidation.journeyType==4)
        {
            if(orderValidation.internationalValues.equals("document"))
            {
                maxWeight=5
            }
            else if(orderValidation.internationalValues.equals("parcel"))
            {
                maxWeight=1000
            }
        }

        var weightInt=weight.toInt()
        if(type==1)
        {
            if(maxWeight!=weightInt)
            weightInt=weightInt+increment.toInt();
        }
        else
        {
            if(weightInt>0)
            {
                weightInt=weightInt-increment.toInt();
            }
        }
        return weightInt
    }

}