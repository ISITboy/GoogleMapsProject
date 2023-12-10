package com.example.googlemapsproject.data.remote.dto.distances

data class Metadata(
    val attribution: String,
    val engine: Engine,
    val query: Query,
    val service: String,
    val timestamp: Long
)