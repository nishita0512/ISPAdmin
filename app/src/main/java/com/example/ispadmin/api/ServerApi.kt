package com.example.ispadmin.api

import com.example.ispadmin.model.Server
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ServerApi {

    @GET("getAllServers")
    suspend fun getAllServers(): ArrayList<Server>

    @GET("getServerById/{serverId}")
    suspend fun getServerById(@Path("serverId") serverId: Int): Server

    @Multipart
    @POST("updateServer")
    suspend fun updateServer(@PartMap fields: HashMap<String, RequestBody>): ResponseBody

    @Multipart
    @POST("addServer")
    suspend fun addServer(@PartMap fields: HashMap<String, RequestBody>): Server

}