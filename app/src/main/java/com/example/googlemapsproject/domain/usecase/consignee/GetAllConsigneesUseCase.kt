package com.example.googlemapsproject.domain.usecase.consignee

import com.example.googlemapsproject.domain.repository.ConsigneeRepository
import javax.inject.Inject

class GetAllConsigneesUseCase @Inject constructor(
    private val consigneeRepository: ConsigneeRepository
) {
    operator fun invoke() = consigneeRepository.getAllConsignee()
}