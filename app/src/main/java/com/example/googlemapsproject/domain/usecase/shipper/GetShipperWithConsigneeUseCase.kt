package com.example.googlemapsproject.domain.usecase.shipper

import com.example.googlemapsproject.domain.repository.ShipperRepository
import javax.inject.Inject

class GetShipperWithConsigneeUseCase @Inject constructor(
    private val shipperRepository: ShipperRepository
) {
    suspend operator fun invoke() = shipperRepository.getShipperWithConsignee()
}