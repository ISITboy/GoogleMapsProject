package com.example.googlemapsproject.data.repository

import com.example.googlemapsproject.data.remote.RemoteDataSource
import com.example.googlemapsproject.data.remote.dto.distances.DistanceResponse
import com.example.googlemapsproject.data.remote.dto.distances.Query
import com.example.googlemapsproject.data.remote.dto.routes.RouteResponse
import com.example.googlemapsproject.data.remote.utils.BaseApiResponse
import com.example.googlemapsproject.data.remote.utils.NetworkResult
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): BaseApiResponse(){
    suspend fun calculateDistance(body: Query):NetworkResult<DistanceResponse>{
        return safeApiCall { remoteDataSource.calculateDistance(body = body)}
    }

    suspend fun getRoute(start:String, end:String):NetworkResult<RouteResponse>{
        return safeApiCall { remoteDataSource.getRoute(start = start, end = end)}
    }

}