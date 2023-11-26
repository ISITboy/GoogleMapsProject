package com.example.googlemapsproject.domain.repository

import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.models.ShipperWithConsignee
import kotlinx.coroutines.flow.Flow

interface ShipperRepository {

    suspend fun insertShipper(shipper: Shipper)
    suspend fun deleteShipper(shipper: Shipper)
    fun getAllShippers(): Flow<List<Shipper>>
    suspend fun getShipper(id: Int): Shipper?
    suspend fun getShipperWithConsignee():List<ShipperWithConsignee>
}