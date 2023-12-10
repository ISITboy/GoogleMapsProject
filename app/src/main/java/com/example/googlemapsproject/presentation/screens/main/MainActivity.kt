package com.example.googlemapsproject.presentation.screens.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.googlemapsproject.R
import com.example.googlemapsproject.algorithm.ClarkeRightAlgorithm
import com.example.googlemapsproject.algorithm.DataForClarkeRight
import com.example.googlemapsproject.algorithm.getKilometerWinningMatrix
import com.example.googlemapsproject.algorithm.getListKilometerWinningMatrix
import com.example.googlemapsproject.algorithm.getShortestPathMatrix
import com.example.googlemapsproject.data.remote.dto.distances.Query
import com.example.googlemapsproject.data.remote.dto.routes.RouteResponse
import com.example.googlemapsproject.databinding.ActivityMainBinding
import com.example.googlemapsproject.domain.models.Shipper
import com.example.googlemapsproject.presentation.screens.main.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap

    private val viewModel: MainViewModel by viewModels()
    private var listWithDistance: Array<Array<Double>> = arrayOf()
    private val listLatLng: MutableList<LatLng> = mutableListOf()

    var lShipper: List<Shipper>? = null

    val allLatLng : MutableList<LatLng> = mutableListOf()

    private var start: String = ""
    private var end: String = ""

    var poly: Polyline? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFragment()
        var countShipper:Int? = null
        viewModel.markers.observe(this) { pair ->
            pair.first.forEach { markerOptions ->
                map.addMarker(markerOptions)
            }
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(pair.second.last(), 18f),
                2000,
                null
            )
            countShipper = pair.second.size
            unionShipperAndConsigneeList(pair.second,viewModel.markersC.value?.second.orEmpty())
        }

        viewModel.markersC.observe(this){pair->
            pair.first.forEach { markerOptions ->
                map.addMarker(markerOptions)
            }

            unionShipperAndConsigneeList(viewModel.markers.value?.second.orEmpty(),pair.second)
        }


        viewModel.getDistances.observe(this) { distance ->
            CoroutineScope(Dispatchers.IO).launch {
                listWithDistance = distance.map { it.toTypedArray() }.toTypedArray()
                readMatrix(listWithDistance)
                println("shortNatrix)")
                readMatrix(getShortestPathMatrix(listWithDistance))
                val resultArray = getListKilometerWinningMatrix(
                    0 until countShipper!!,
                    getShortestPathMatrix(listWithDistance)
                )
                println("matrix)shortNatrix")
                readMatrix(resultArray.last())

                val kilometersWinMatrix = mutableListOf<MutableList<MutableList<Double>>>()
                resultArray.forEach {
                    kilometersWinMatrix.add(getKilometerWinningMatrix(it.map { it.toTypedArray() }
                        .toTypedArray()))
                }
                println("KilometerWinningMatri)")
                kilometersWinMatrix.forEach { readMatrix(it) }

                val volumes = viewModel.getVolumes.value
                val max = 6000.0
                val listItem = mutableListOf<ClarkeRightAlgorithm>()
                kilometersWinMatrix.forEach {
                    listItem.add(
                        ClarkeRightAlgorithm(
                            DataForClarkeRight(
                                matrix = makeMatrixWithoutGO(it).map { it.toMutableList() }.toMutableList(),
                                cargoVolumes = volumes!!,
                                carrying = max
                            )
                        )
                    )
                }
                var checkDeleteWeight = ArrayList<Int>()
                while (listItem.first().cargoVolumes.max() != 0.0) {
                    listItem.forEachIndexed { i, v ->
                        if (findItemWithMaxValue(listItem) == i) {
                            v.finish = false
                            v.start()
                            v.cargoVolumes.forEachIndexed { index, d ->
                                if (d == 0.0) {
                                    checkDeleteWeight.add(index)
                                }
                            }
                        }
                        v.listRoutes.forEach {
                            Log.d("MyLog", "rrrrrrrrrr${it}")
                            it
                        }

                    }
                    checkDeleteWeight.forEach { n ->
                        listItem.forEach {
                            it.putNullInMatrix(n)
                        }
                    }
                }
                repeat(countShipper!!){
                    val listLatLng = mutableListOf<LatLng>()
                    listLatLng.add(allLatLng[it])
                    for (i in 0 until allLatLng.size){
                        listItem[it].listRoutes.forEach {
                            if (i in it){
                                listLatLng.add(allLatLng[i+countShipper!!])
                            }
                        }
                    }
                    if(it==0){
                        createRoute(listLatLng)
                    }else{
                        createRoute1(listLatLng)
                    }

                    listLatLng.clear()
                }
            }


        }
        viewModel.getAllAddressesAndDrawMarkersShipper()
        viewModel.getAllAddressesAndDrawMarkersConsignee()
    }

    fun makeMatrixWithoutGO(matrix: MutableList<MutableList<Double>>): Array<Array<Double>> {
        val curMatrix: Array<Array<Double>> = Array(matrix.size - 1) { Array(matrix.size - 1) { Int.MAX_VALUE.toDouble() } }
        for (i in curMatrix.indices) {
            for (j in 0 until curMatrix[i].size) {
                curMatrix[i][j] = matrix[i + 1][j + 1]
            }
        }
        return curMatrix
    }



    fun readMatrix(matrix:Array<Array<Double>>){
        Log.d("MyLog","matrix rout")
        for (i in 0 until matrix.size) {
            for(j in 0 until  matrix[i].size){
                print("${matrix[i][j]} \t")
            }
            println()
        }
    }
    fun readMatrix(matrix:List<List<Double>>){
        Log.d("MyLog","matrix LtnLtd")
        for (i in 0 until matrix.size) {
            for(j in 0 until  matrix[i].size){
                //Log.d("MyLog","${matrix[i][j]} \t")
                print("${matrix[i][j]} \t")
            }
            println()
        }
    }


    fun findItemWithMaxValue(listItem: MutableList<ClarkeRightAlgorithm>): Int {
        var list = mutableListOf<Double>()
        for (i in listItem) {
            list.add(i.returnCheckMaxValues())
            println("maxValue.${i.returnCheckMaxValues()}")
        }
        return list.withIndex().maxBy { it.value }.index
    }

    private fun unionShipperAndConsigneeList(shipper: List<LatLng>,consignee: List<LatLng>){
        val combinedList = mutableListOf<LatLng>()
        if (shipper.isNotEmpty() && consignee.isNotEmpty()) {
            combinedList.addAll(shipper)
            combinedList.addAll(consignee)
            // Если оба списка не пустые, то выполняем отправку данных на сервер
            allLatLng.addAll(combinedList)

            sendDataToServer(combinedList)
        }
    }
    private fun sendDataToServer(data:MutableList<LatLng>){
        viewModel.calculateDistance(
            Query(
                locations = viewModel.createListLocation(data),
                metrics = listOf("distance")
            )
        )
    }
    private suspend fun createRoute(list: MutableList<LatLng>) {
        println("list: MutableList<LatLng> $list")
            val currentList = ArrayList(list)
            for(i in 0 until currentList.size-1) {
                val call = viewModel.getRouteUseCase.invoke(
                    start = "${currentList[i].longitude},${currentList[i].latitude}",
                    end = "${currentList[i+1].longitude},${currentList[i+1].latitude}"
                )
                drawRoute(call.data)
            }

    }

    private suspend fun createRoute1(list: MutableList<LatLng>) {
        println("list: MutableList<LatLng> $list")
        val currentList = ArrayList(list)
        for(i in 0 until currentList.size-1) {
            val call = viewModel.getRouteUseCase.invoke(
                start = "${currentList[i].longitude},${currentList[i].latitude}",
                end = "${currentList[i+1].longitude},${currentList[i+1].latitude}"
            )
            drawRoute1(call.data)
        }

    }

    private fun drawRoute(routeResponse: RouteResponse?) {
        val polyLineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polyLineOptions.add(LatLng(it[1], it[0]))
        }
        polyLineOptions.color(Color.GREEN)
        println("routeResponse = $routeResponse")
        runOnUiThread {
            poly = map.addPolyline(polyLineOptions)
        }
    }

    private fun drawRoute1(routeResponse: RouteResponse?) {
        val polyLineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polyLineOptions.add(LatLng(it[1], it[0]))
        }
        polyLineOptions.color(Color.BLUE)
        println("routeResponse = $routeResponse")
        runOnUiThread {
            poly = map.addPolyline(polyLineOptions)
        }
    }

    private fun createFragment() = with(binding) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MainActivity)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this@MainActivity)
        map.setOnMyLocationClickListener(this@MainActivity)
        enableLocation()
    }

    private fun createMarker(l: MutableList<Address>) {
        l?.forEach {
            Log.d("MyLog", "${it.latitude}${it.longitude}")
            val marker = MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("123")
            map.addMarker(marker)
        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(this, "Зайдите в настройки и примите разрешения", Toast.LENGTH_SHORT)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()) {
            map.isMyLocationEnabled = false
            Toast.makeText(
                this,
                "Para activar la localización ve a ajustes y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Чтобы активировать местоположение, перейдите в настройки и примите разрешения.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "мои координаты ${p0.latitude} ${p0.longitude}", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "поиск меня", Toast.LENGTH_SHORT).show()
        return false
    }


    companion object {
        const val LOCATION_REQUEST_CODE = 0
        const val API_KEY = "5b3ce3597851110001cf6248f7217712cace468f84e68e74ce0563df"
    }
}
