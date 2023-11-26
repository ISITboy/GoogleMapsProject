package com.example.googlemapsproject.presentation.screens.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.googlemapsproject.ApiService
import com.example.googlemapsproject.R
import com.example.googlemapsproject.RouteResponse
import com.example.googlemapsproject.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var map :GoogleMap

    private var start: String = ""
    private var end: String = ""

    var poly: Polyline? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFragment()
    }

    private fun createFragment()= with(binding){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MainActivity)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        map.setOnMyLocationButtonClickListener(this@MainActivity)
        map.setOnMyLocationClickListener(this@MainActivity)
        enableLocation()
    }
    private fun createMarker(){
        val coordinates = LatLng(53.684183617578014, 23.83613351299882)
        val marker = MarkerOptions().position(coordinates).title("My park")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            2000,
            null
        )
    }
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Зайдите в настройки и примите разрешения", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if(!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_REQUEST_CODE -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Чтобы активировать местоположение, перейдите в настройки и примите разрешения.", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "мои координаты ${p0.latitude} ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "поиск меня", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun findLocation(){
        CoroutineScope(Dispatchers.IO).launch {
        val geoCoder = Geocoder(this@MainActivity,Locale.getDefault())
        var addressList: List<Address>? = null
        addressList = geoCoder.getFromLocationName("улица Независимости, 1, Минск, Беларусь", 1)
        val address = addressList!![0]
        start = "${address.longitude},${address.latitude}"
        addressList = geoCoder.getFromLocationName("ул. Дубко 17, Гродно", 1)
        val addres = addressList!![0]
        end = "${addres.longitude},${addres.latitude}"
//        val latLng = LatLng(address.latitude, address.longitude)
//        map!!.addMarker(MarkerOptions().position(latLng).title("OldCity"))
//        map!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        Log.i("MyLog", "start: $start; end: $end")
        }
    }



    fun fundLocation(view: View) {
        findLocation()
        createRoute()
    }

    private fun createRoute() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java)
                .getRoute(API_KEY, start, end)
            val call1 = getRetrofit().create(ApiService::class.java)
                .getRoute(API_KEY, "23.83603926043142,53.68420072816951", "23.8316030981722,53.68528060138215")
            if (call.isSuccessful) {
                Log.i("MyLog", "call.isSuccessful ${call.isSuccessful}")
                drawRoute(call.body())
            }
            if (call1.isSuccessful) {
                Log.i("MyLog", "call.isSuccessful ${call1.isSuccessful}")
                drawRoute(call1.body())
            }
            else {
                Log.i("MyLog", "KO")
            }
        }
    }
    private fun drawRoute(routeResponse: RouteResponse?) {
        val polyLineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polyLineOptions.add(LatLng(it[1], it[0]))
        }
        runOnUiThread {
            poly = map.addPolyline(polyLineOptions)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object{
        const val LOCATION_REQUEST_CODE = 0
        const val API_KEY = "5b3ce3597851110001cf6248f7217712cace468f84e68e74ce0563df"
    }
}
