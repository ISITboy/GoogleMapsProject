package com.example.googlemapsproject.data.repository

import com.example.googlemapsproject.data.local.database.ConsigneeDao
import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.domain.repository.ConsigneeRepository
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsigneeRepositoryImpl @Inject constructor(
    private val consigneeDao: ConsigneeDao
) :ConsigneeRepository{
    override suspend fun insert(consignee: Consignee) {
        consigneeDao.insert(consignee)
    }

    override suspend fun delete(consignee: Consignee) {
        consigneeDao.delete(consignee)
    }

    override fun getAllConsignee(): Flow<List<Consignee>> = consigneeDao.getAllConsignee()

    override suspend fun getConsignee(id: Int): Consignee? = consigneeDao.getConsignee(id)

}