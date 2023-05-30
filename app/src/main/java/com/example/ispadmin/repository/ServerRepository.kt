package com.example.ispadmin.repository

import com.example.ispadmin.api.RetrofitInstance
import com.example.ispadmin.model.Server
import okhttp3.RequestBody
import okhttp3.ResponseBody

class ServerRepository {

    suspend fun getAllServers(): ArrayList<Server> {
        return RetrofitInstance.serverApi.getAllServers()
    }

    suspend fun getServerById(serverId: Int): Server {
        return RetrofitInstance.serverApi.getServerById(serverId)
    }

    suspend fun updateServer(fields: HashMap<String, RequestBody>): ResponseBody {
        return RetrofitInstance.serverApi.updateServer(fields)
    }

    suspend fun addServer(fields: HashMap<String, RequestBody>): Server {
        return RetrofitInstance.serverApi.addServer(fields)
    }

}