package com.example.googlemapsproject.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.googlemapsproject.data.local.ConsigneeDao
import com.example.googlemapsproject.data.local.Database
import com.example.googlemapsproject.data.local.ShipperDao
import com.example.googlemapsproject.data.repository.ConsigneeRepositoryImpl
import com.example.googlemapsproject.data.repository.ShipperRepositoryImpl
import com.example.googlemapsproject.domain.Constants.DATABASE_NAME
import com.example.googlemapsproject.domain.repository.ConsigneeRepository
import com.example.googlemapsproject.domain.repository.ShipperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): Database {
        return Room.databaseBuilder(
            context = application,
            klass = Database::class.java,
            name = DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideShipperDao(
        database: Database
    ): ShipperDao = database.shipperDao

    @Provides
    @Singleton
    fun provideConsigneeDao(
        database: Database
    ): ConsigneeDao = database.consigneeDao

    @Provides
    @Singleton
    fun providesShipperRepository(
        shipperDao: ShipperDao
    ):ShipperRepository = ShipperRepositoryImpl(shipperDao = shipperDao)

    @Provides
    @Singleton
    fun providesConsigneeRepository(
        consigneeDao: ConsigneeDao
    ): ConsigneeRepository = ConsigneeRepositoryImpl(consigneeDao = consigneeDao)

}