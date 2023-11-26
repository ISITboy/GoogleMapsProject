package com.example.googlemapsproject.domain.usecase.consignee

import com.example.googlemapsproject.domain.repository.ConsigneeRepository
import javax.inject.Inject

class GetConsigneeUseCase @Inject constructor(
    private val consigneeRepository: ConsigneeRepository
) {
    suspend operator fun invoke(id:Int){
        consigneeRepository.getConsignee(id)
    }
}