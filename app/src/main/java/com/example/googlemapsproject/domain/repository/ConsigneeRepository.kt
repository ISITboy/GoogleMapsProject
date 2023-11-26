package com.example.googlemapsproject.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.googlemapsproject.domain.Constants
import com.example.googlemapsproject.domain.models.Consignee
import kotlinx.coroutines.flow.Flow

interface ConsigneeRepository {
    suspend fun insert(consignee: Consignee)
    suspend fun delete(consignee: Consignee)
    fun getAllConsignee(): Flow<List<Consignee>>
    suspend fun getConsignee(id: Int): Consignee?
}