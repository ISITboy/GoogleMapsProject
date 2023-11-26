package com.example.googlemapsproject.domain.usecase.shipper

import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.repository.ShipperRepository
import javax.inject.Inject

class DeleteShipperUseCase @Inject constructor(
    private val shipperRepository: ShipperRepository
) {
    suspend operator fun invoke(shipper: Shipper){
        shipperRepository.deleteShipper(shipper)
    }
}