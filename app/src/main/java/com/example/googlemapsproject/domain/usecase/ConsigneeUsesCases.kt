package com.example.googlemapsproject.domain.usecase

import com.example.googlemapsproject.domain.usecase.consignee.DeleteConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.consignee.InsertConsigneeUseCase
import javax.inject.Inject

data class ConsigneeUsesCases @Inject constructor(
    val deleteConsigneeUseCase: DeleteConsigneeUseCase,
    val insertConsigneeUseCase: InsertConsigneeUseCase,
    val getAllConsigneeUseCase: InsertConsigneeUseCase,
    val getConsigneeUseCase: InsertConsigneeUseCase
)