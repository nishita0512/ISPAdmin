package com.example.ispadmin.api

import com.example.ispadmin.model.Customer
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface CustomerApi {

    @GET("getCustomerById/{custId}")
    suspend fun getCustomerById(@Path("custId") custId: Int): Customer

    @GET("getAllCustomers")
    suspend fun getAllCustomers(): ArrayList<Customer>

    @Multipart
    @POST("addCustomer")
    suspend fun addCustomer(@PartMap fields: HashMap<String, RequestBody>): Customer

    @Multipart
    @POST("updateCustomer")
    suspend fun updateCustomer(@PartMap fields: HashMap<String, RequestBody>): ResponseBody

}