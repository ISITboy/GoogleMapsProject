package com.example.googlemapsproject.domain.usecase

import com.example.googlemapsproject.domain.usecase.shipper.DeleteShipperUseCase
import com.example.googlemapsproject.domain.usecase.shipper.InsertShipperUseCase
import javax.inject.Inject

data class ShipperUsesCases @Inject constructor(
    val deleteShipperUseCase: DeleteShipperUseCase,
    val insertShipperUseCase: InsertShipperUseCase,
    val getAllShipperUseCase: InsertShipperUseCase,
    val getShipperUseCase: InsertShipperUseCase
)