package com.example.googlemapsproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.googlemapsproject.domain.Constants.TABLE_SHIPPER
import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.domain.models.ShipperWithConsignee
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shipper: Shipper)
    @Delete
    suspend fun delete(shipper: Shipper)
    @Query("SELECT * FROM $TABLE_SHIPPER")
    fun getAllShippers(): Flow<List<Shipper>>
    @Query("SELECT * FROM $TABLE_SHIPPER WHERE id=:id")
    suspend fun getShipper(id: Int): Shipper?
    @Transaction
    @Query("SELECT * FROM $TABLE_SHIPPER")
    suspend fun getShipperWithConsignee():List<ShipperWithConsignee>

}