package com.example.googlemapsproject.data.repository

import com.example.googlemapsproject.data.local.ShipperDao
import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.models.ShipperWithConsignee
import com.example.googlemapsproject.domain.repository.ShipperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShipperRepositoryImpl @Inject constructor(
    private val shipperDao: ShipperDao
) : ShipperRepository {
    override suspend fun insertShipper(shipper: Shipper) {
        shipperDao.insert(shipper)
    }

    override suspend fun deleteShipper(shipper: Shipper) {
        shipperDao.delete(shipper)
    }

    override fun getAllShippers(): Flow<List<Shipper>> = shipperDao.getAllShippers()

    override suspend fun getShipper(id: Int): Shipper? = shipperDao.getShipper(id)

    override suspend fun getShipperWithConsignee(): List<ShipperWithConsignee> =
        shipperDao.getShipperWithConsignee()
}