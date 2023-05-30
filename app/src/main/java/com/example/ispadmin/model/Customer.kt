package com.example.ispadmin.model

data class Customer(
    val custId: Int,
    val hashedPassword: String,
    val name: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val phoneNo: String,
    val email: String,
    val customerSince: Long,
    val planEndDate: Long,
    val planId: Int,
    val isActive: Int,
    val dataUsed: Long
)