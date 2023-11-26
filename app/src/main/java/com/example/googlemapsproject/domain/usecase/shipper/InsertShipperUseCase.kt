package com.example.googlemapsproject.domain.usecase.shipper

import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.repository.ShipperRepository
import javax.inject.Inject

class InsertShipperUseCase @Inject constructor(
    private val shipperRepository: ShipperRepository
) {
    suspend operator fun invoke(shipper: Shipper) {
        shipperRepository.insertShipper(shipper)
    }
}