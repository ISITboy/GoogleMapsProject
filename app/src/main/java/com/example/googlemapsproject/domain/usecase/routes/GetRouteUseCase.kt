package com.example.googlemapsproject.domain.usecase.routes

import com.example.googlemapsproject.data.remote.dto.distances.Query
import com.example.googlemapsproject.data.repository.RouteRepository
import javax.inject.Inject

class GetRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository

) {
    suspend fun invoke(start :String, end : String)= routeRepository.getRoute(start = start, end = end)
}