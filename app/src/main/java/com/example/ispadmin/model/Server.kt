package com.example.ispadmin.model

data class Server(
    val id: Int,
    val longitude: Double,
    val latitude: Double,
    val type: String,
    val isActive: Int,
    val loadOnServer: Int
)