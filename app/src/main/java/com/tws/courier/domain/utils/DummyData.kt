package com.tws.courier.domain.utils

import com.tws.courier.domain.models.Price
import com.tws.courier.domain.models.VehicleType

fun getVehicleType(): ArrayList<VehicleType> {
    return ArrayList<VehicleType>().apply {
        add(
            VehicleType(
                 "Bike"
            )
        )

        add(
            VehicleType(
                "Mini truck"
            )
        )

        add(
            VehicleType(
                "Big Truck"
            )
        )

        add(
            VehicleType(
                "Train"
            )
        )

        add(
            VehicleType(
                "National Flight (Silver)"
            )
        )

    }

}

fun getPrice(): Price
{
    return  Price(
        "2.5",
        "2.5",
        "0",
        "10",
        "2.5",
        "2.5",
        "70",
        "30",
        VehicleType(
            "Bike"
        )
    )
}
