package com.example.googlemapsproject.domain.usecase

import com.example.googlemapsproject.domain.usecase.consignee.DeleteConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.consignee.GetAllConsigneesUseCase
import com.example.googlemapsproject.domain.usecase.consignee.GetConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.consignee.InsertConsigneeUseCase
import javax.inject.Inject

data class ConsigneeUsesCases @Inject constructor(
    val deleteConsigneeUseCase: DeleteConsigneeUseCase,
    val insertConsigneeUseCase: InsertConsigneeUseCase,
    val getAllConsigneeUseCase: GetAllConsigneesUseCase,
    val getConsigneeUseCase: GetConsigneeUseCase
)