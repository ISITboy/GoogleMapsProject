package com.example.googlemapsproject.presentation.screens.storagemanager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.usecase.ConsigneeUsesCases
import com.example.googlemapsproject.domain.usecase.ShipperUsesCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddManagerStorageViewModel @Inject constructor(
    private val consigneeUsesCases: ConsigneeUsesCases,
    private val shipperUsesCases: ShipperUsesCases
):ViewModel() {
    fun insertShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.insertShipperUseCase(shipper = shipper)
    }
    fun insertConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.insertConsigneeUseCase(consignee = consignee)
    }

}