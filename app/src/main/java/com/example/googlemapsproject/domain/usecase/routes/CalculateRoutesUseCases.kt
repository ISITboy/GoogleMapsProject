package com.example.googlemapsproject.domain.usecase.routes

import com.example.googlemapsproject.data.remote.dto.distances.Query
import com.example.googlemapsproject.data.repository.RouteRepository
import javax.inject.Inject

class CalculateRoutesUseCases @Inject constructor(
    private val routeRepository: RouteRepository

) {
    suspend fun invoke(body: Query)= routeRepository.calculateDistance(body=body)
}