package com.example.googlemapsproject.domain.usecase.consignee

import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.domain.repository.ConsigneeRepository
import javax.inject.Inject

class InsertConsigneeUseCase @Inject constructor(
    private val consigneeRepository: ConsigneeRepository
) {
    suspend operator fun invoke(consignee: Consignee) {
        consigneeRepository.insert(consignee)
    }
}