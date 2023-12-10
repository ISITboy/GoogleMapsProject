package com.example.googlemapsproject.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.domain.models.Shipper

@Database(entities = [Consignee::class,Shipper::class],version = 1,)
abstract class Database :RoomDatabase(){
    abstract val shipperDao: ShipperDao
    abstract val consigneeDao: ConsigneeDao
}