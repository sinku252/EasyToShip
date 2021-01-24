package com.tws.courier.domain.models

import com.google.android.gms.maps.model.LatLng

data class OrderValidation(
    var journeyType:Int=0,//Bike,Local truck,domestic,internation
    var startLatLng: LatLng? =null,
    var endLatLng: LatLng? =null,
    var transmissionSpeed: String="",
    var paymentMode: String="",
    var origin: String="",
    var destination: String="",

    var bikeValues: BikeValues= BikeValues(),
    var localTruckValues: LocalTruckValues = LocalTruckValues(),
    var domesticValues: DomesticValues = DomesticValues(),
    var internationalValues: InternationalValues = InternationalValues()

)

data class BikeValues(
    var fromAddress:String="",
    var toAddress:String="",
    var shipmentType:String="",//Document,Parcel
    var selectDate:String="",
    var pickupMobile:String="",
    var deliveryMobile:String="",
    var weight:String="",
    var insurance:String="0",
    var dc:String="0"
)
data class LocalTruckValues(
    var fromAddress:String="",
    var toAddress:String="",
    var shipmentType:String="",//Parcel
    var oneWayTwoWay:String="",
    var pickupMobile:String="",
    var deliveryMobile:String="",
    var selectDate:String="",
    var weight:String="",
    var personalOrCommercial:String="",
    var insurance:String="0",
    var dc:String="0",
    var porter:String="0",//if check values for porter
    var porterValue:String=""//popup panding
)

data class DomesticValues(
    var domesticType:String="",//flight,train,truck
    var domesticTruckValues: DomesticTruckValues? = DomesticTruckValues(),
    var domesticTrainValues: DomesticTrainValues? = DomesticTrainValues(),
    var domesticFlightValues: DomesticFlightValues? = DomesticFlightValues()


)
data class DomesticFlightValues(
    var shipmentType:String="",//Document,Parcel
    var fromAddress:String="",
    var toAddress:String="",
    var weight:String="",
    var dimention:String="", //if shipmentType Parcel
    var pickupMobile:String="",
    var deliveryMobile:String="",
    var selectDate:String="",
    var insurance:String="0",//if check personalOrCommercial
    var personalOrCommercial:String=""
)
data class DomesticTrainValues(
    var shipmentType:String="",//Parcel
    var fromAddress:String="",
    var toAddress:String="",
    var weight:String="",
    var dimention:String="",
    var pickupMobile:String="",
    var deliveryMobile:String="",
    var selectDate:String="",
    var insurance:String="0",
    var dc:String="0",//if check personalOrCommercial
    var personalOrCommercial:String=""
)
data class DomesticTruckValues(
    var shipmentType:String="",//Parcel
    var fromAddress:String="",
    var toAddress:String="",
    var weight:String="",
    var dimention:String="",
    var pickupMobile:String="",
    var deliveryMobile:String="",
    var selectDate:String="",
    var insurance:String="0",
    var dc:String="0",//if check personalOrCommercial
    var personalOrCommercial:String=""
)

data class InternationalValues(
    var shipmentType:String="",//Parcel
    var importExport:String="",
    var fromAddress:String="",
    var toAddress:String="",
    var weight:String="",
    var dimention:String="",//if Parcel
    var pickupMobile:String="",
    var deliveryMobile:String="",
    var selectDate:String="",
    var insurance:String="0",//if check personalOrCommercial
    var personalOrCommercial:String=""
)