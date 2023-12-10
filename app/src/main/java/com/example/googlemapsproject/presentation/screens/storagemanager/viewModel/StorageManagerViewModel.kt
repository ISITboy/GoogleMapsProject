package com.example.googlemapsproject.presentation.screens.storagemanager.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.usecase.ConsigneeUsesCases
import com.example.googlemapsproject.domain.usecase.ShipperUsesCases
import com.example.googlemapsproject.presentation.screens.storagemanager.event.ManagerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageManagerViewModel @Inject constructor(
    private val consigneeUsesCases: ConsigneeUsesCases,
    private val shipperUsesCases: ShipperUsesCases
) : ViewModel() {


    init{
        Log.d("MyLog","consigneeUsesCases ${consigneeUsesCases.toString()}")
    }
    private val shipperItems = MutableLiveData<List<Shipper>>()
    fun getShipper(): LiveData<List<Shipper>> {
        return shipperItems
    }

    private val consigneeItems = MutableLiveData<List<Consignee>>()
    fun getConsignee(): LiveData<List<Consignee>> {
        return consigneeItems
    }

    val event = MutableStateFlow<ManagerEvent>(ManagerEvent.AddShipperItem)
    fun insertShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.insertShipperUseCase(shipper = shipper)
    }

    fun insertConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.insertConsigneeUseCase(consignee = consignee)
    }

    fun deleteShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.deleteShipperUseCase(shipper = shipper)
    }


    fun deleteConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.deleteConsigneeUseCase(consignee = consignee)
    }

    fun getAllShipperItems() = viewModelScope.launch {
        shipperUsesCases.getAllShipperUseCase().collect {
            shipperItems.postValue(it)
            Log.d("MyLog","sizeM: ${shipperItems.value?.size}")
        }
    }

    fun getAllConsigneeItems() = viewModelScope.launch {
        consigneeUsesCases.getAllConsigneeUseCase().collect {
            consigneeItems.postValue(it)
            Log.d("MyLog","sizeM: ${consigneeItems.value?.size}")
        }
    }
}