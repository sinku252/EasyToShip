package com.tws.courier.domain.models

import com.google.android.gms.maps.model.LatLng
import kotlin.properties.Delegates

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
/*{

    var journeyType_: Int by Delegates.observable(journeyType) { prop, old, new ->
        println("Name changed from $old to $new")
        journeyType = new
    }

    var startLatLng_: LatLng? by Delegates.observable(startLatLng) { prop, old, new ->
        println("Name changed from $old to $new")
        startLatLng = new
    }

    var endLatLng_: LatLng? by Delegates.observable(endLatLng) { prop, old, new ->
        println("Name changed from $old to $new")
        endLatLng = new
    }

    var transmissionSpeed_: String by Delegates.observable(transmissionSpeed) { prop, old, new ->
        println("Name changed from $old to $new")
        transmissionSpeed = new
    }

    var paymentMode_: String by Delegates.observable(paymentMode) { prop, old, new ->
        println("Name changed from $old to $new")
        paymentMode = new
    }

    var origin_: String by Delegates.observable(origin) { prop, old, new ->
        println("Name changed from $old to $new")
        origin = new
    }

    var destination_: String by Delegates.observable(destination) { prop, old, new ->
        println("Name changed from $old to $new")
        destination = new
    }

    var bikeValues_: BikeValues by Delegates.observable(bikeValues) { prop, old, new ->
        println("Name changed from $old to $new")
        bikeValues = new
    }

    var localTruckValues_: LocalTruckValues by Delegates.observable(localTruckValues) { prop, old, new ->
        println("Name changed from $old to $new")
        localTruckValues = new
    }

    var domesticValues_: DomesticValues by Delegates.observable(domesticValues) { prop, old, new ->
        println("Name changed from $old to $new")
        domesticValues = new
    }

    var internationalValues_: InternationalValues by Delegates.observable(internationalValues) { prop, old, new ->
        println("Name changed from $old to $new")
        internationalValues = new
    }

}*/

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