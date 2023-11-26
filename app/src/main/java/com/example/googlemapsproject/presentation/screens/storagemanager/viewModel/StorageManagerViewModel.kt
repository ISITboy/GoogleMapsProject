package com.example.googlemapsproject.presentation.screens.storagemanager.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.usecase.ConsigneeUsesCases
import com.example.googlemapsproject.domain.usecase.ShipperUsesCases
import com.example.googlemapsproject.domain.usecase.shipper.GetShipperWithConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.shipper.InsertShipperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StorageManagerViewModel @Inject constructor(
    private val consigneeUsesCases: ConsigneeUsesCases,
    private val shipperUsesCases: ShipperUsesCases
) : ViewModel() {


    fun deleteShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.deleteShipperUseCase(shipper = shipper)
    }


    fun deleteConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.deleteConsigneeUseCase(consignee = consignee)
    }
}