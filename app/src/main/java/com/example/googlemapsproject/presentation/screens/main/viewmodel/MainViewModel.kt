package com.example.googlemapsproject.presentation.screens.main.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapsproject.R
import com.example.googlemapsproject.data.remote.dto.distances.Query
import com.example.googlemapsproject.domain.usecase.ConsigneeUsesCases
import com.example.googlemapsproject.domain.usecase.ShipperUsesCases
import com.example.googlemapsproject.domain.usecase.routes.CalculateRoutesUseCases
import com.example.googlemapsproject.domain.usecase.routes.GetRouteUseCase
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val consigneeUsesCases: ConsigneeUsesCases,
    private val shipperUsesCases: ShipperUsesCases,
    @ApplicationContext private val appContext: Context,
    private val calculateRoutesUseCases: CalculateRoutesUseCases,
    val getRouteUseCase: GetRouteUseCase
):ViewModel() {



    private val _distances: MutableLiveData<List<List<Double>>> = MutableLiveData()
    val getDistances: LiveData<List<List<Double>>> = _distances

    private val _volumes : MutableLiveData<List<Double>> = MutableLiveData()
    val getVolumes : LiveData<List<Double>> = _volumes

    private val _markers: MutableLiveData<Pair<List<MarkerOptions>,MutableList<LatLng>>> = MutableLiveData()
    val markers: LiveData<Pair<List<MarkerOptions>,MutableList<LatLng>>> = _markers

    private val _markersC: MutableLiveData<Pair<List<MarkerOptions>,MutableList<LatLng>>> = MutableLiveData()
    val markersC: LiveData<Pair<List<MarkerOptions>,MutableList<LatLng>>> = _markersC

    fun getAllAddressesAndDrawMarkersShipper() {
        viewModelScope.launch {
            shipperUsesCases.getAllShipperUseCase().collect {
                val markersList = mutableListOf<MarkerOptions>()
                var coordinateShipper :MutableList<LatLng> = mutableListOf()
                it.forEach { shipper ->
                    val latLng = getLatLngFromAddress(shipper.address)
                    latLng?.let {
                        val markerOptions = MarkerOptions().position(it)
                            .title(shipper.address)
                            .icon(getBitmapDescriptorFromVector(context = appContext, R.drawable.ic_storehouse))
                        coordinateShipper.add(it)
                        markersList.add(markerOptions)
                    }
                }
                _markers.postValue(Pair(markersList,coordinateShipper))
            }
        }
    }
    fun getAllAddressesAndDrawMarkersConsignee() {
        viewModelScope.launch {
            consigneeUsesCases.getAllConsigneeUseCase().collect {
                val markersList = mutableListOf<MarkerOptions>()
                var coordinateShipper :MutableList<LatLng> = mutableListOf()
                it.forEach { consignee ->
                    val latLng = getLatLngFromAddress(consignee.address)
                    latLng?.let {
                        val markerOptions = MarkerOptions().position(it)
                            .title(consignee.address)
                            .icon(getBitmapDescriptorFromVector(context = appContext, R.drawable.ic_consignee))
                        coordinateShipper.add(it)
                        markersList.add(markerOptions)
                    }
                }
                _volumes.value = it.map { consignee ->  consignee.volume}
                _markersC.postValue(Pair(markersList,coordinateShipper))
            }
        }
    }

    private suspend fun getLatLngFromAddress(addressName: String): LatLng? {
        return withContext(Dispatchers.IO) {
            val geocoder = Geocoder(appContext, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(addressName, 1)
            if (addresses!!.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                LatLng(latitude, longitude)
            } else {
                null
            }
        }
    }

    fun calculateDistance(body: Query){
        viewModelScope.launch {
            _distances.value = calculateRoutesUseCases.invoke(body).data?.distances
        }
    }

    fun createListLocation(arrayLatLng:MutableList<LatLng>):List<List<Double>>{
        val result = MutableList(arrayLatLng.size) { listOf(0.0) }
        arrayLatLng.forEachIndexed{ index, LatLng->
            result[index] = listOf(LatLng.longitude,LatLng.latitude)
        }
        println("list coordinate:\n$result")
        return result
    }

    fun getBitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.let {
            val bitmap = Bitmap.createBitmap(
                it.intrinsicWidth,
                it.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        } ?: run {
            throw IllegalArgumentException("Vector drawable not found")
        }
    }

}