package com.example.googlemapsproject.presentation.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.googlemapsproject.data.local.database.ConsigneeDao
import com.example.googlemapsproject.data.local.database.Database
import com.example.googlemapsproject.data.local.database.ShipperDao
import com.example.googlemapsproject.data.remote.RoutesApi
import com.example.googlemapsproject.data.remote.utils.Constants.BASE_URL
import com.example.googlemapsproject.data.repository.ConsigneeRepositoryImpl
import com.example.googlemapsproject.data.repository.ShipperRepositoryImpl
import com.example.googlemapsproject.domain.Constants.DATABASE_NAME
import com.example.googlemapsproject.domain.repository.ConsigneeRepository
import com.example.googlemapsproject.domain.repository.ShipperRepository
import com.example.googlemapsproject.domain.usecase.ConsigneeUsesCases
import com.example.googlemapsproject.domain.usecase.ShipperUsesCases
import com.example.googlemapsproject.domain.usecase.consignee.DeleteConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.consignee.GetAllConsigneesUseCase
import com.example.googlemapsproject.domain.usecase.consignee.GetConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.consignee.InsertConsigneeUseCase
import com.example.googlemapsproject.domain.usecase.shipper.DeleteShipperUseCase
import com.example.googlemapsproject.domain.usecase.shipper.GetAllShippersUseCase
import com.example.googlemapsproject.domain.usecase.shipper.GetShipperUseCase
import com.example.googlemapsproject.domain.usecase.shipper.InsertShipperUseCase
import com.example.googlemapsproject.presentation.screens.main.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun providesShipperUsesCases(
        deleteShipperUseCase: DeleteShipperUseCase,
        insertShipperUseCase: InsertShipperUseCase,
        getAllShipperUseCase: GetAllShippersUseCase,
        getShipperUseCase: GetShipperUseCase
    ): ShipperUsesCases = ShipperUsesCases(
        deleteShipperUseCase,insertShipperUseCase,getAllShipperUseCase,getShipperUseCase)
    @Provides
    @Singleton
    fun providesConsigneeUsesCases(
        deleteConsigneeUseCase: DeleteConsigneeUseCase,
        insertConsigneeUseCase: InsertConsigneeUseCase,
        getAllConsigneeUseCase: GetAllConsigneesUseCase,
        getConsigneeUseCase: GetConsigneeUseCase
    ): ConsigneeUsesCases = ConsigneeUsesCases(
        deleteConsigneeUseCase,insertConsigneeUseCase,getAllConsigneeUseCase,getConsigneeUseCase)
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesPostService(retrofit: Retrofit) = retrofit.create(RoutesApi::class.java)
}