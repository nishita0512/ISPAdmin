package com.example.ispadmin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ispadmin.model.Server
import com.example.ispadmin.repository.ServerRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ServerViewModel(private val serverRepository: ServerRepository): ViewModel() {

    val myResponseList: MutableLiveData<ArrayList<Server>> = MutableLiveData()
    val myResponse: MutableLiveData<Server> = MutableLiveData()
    val myResponseString: MutableLiveData<String> = MutableLiveData()

    fun getAllServers(){
        viewModelScope.launch {
            try{
                val response = serverRepository.getAllServers()
                println("GetAllServers"+response[0])
                myResponseList.value = response
            }
            catch(e: Exception){
                Log.d("GetAllServers",e.message.toString())
                myResponseList.value = ArrayList()
            }

        }
    }

    fun getServerById(serverId: Int){
        viewModelScope.launch {
            try{
                val response = serverRepository.getServerById(serverId)
                myResponse.value = response
            }
            catch(e: Exception){
                myResponse.value = Server(-1,0.0,0.0,"",0,0)
            }

        }
    }

    fun updateServer(fields: HashMap<String, RequestBody>){
        viewModelScope.launch {
            try{
                val response = serverRepository.updateServer(fields)
                myResponseString.value = response.string()
            }
            catch(e: Exception){
                myResponseString.value = "Failed"
            }

        }
    }

    fun addServer(fields: HashMap<String, RequestBody>){
        viewModelScope.launch {
            try{
                val response = serverRepository.addServer(fields)
                myResponse.value = response
            }
            catch(e: Exception){
                myResponse.value = Server(-1,0.0,0.0,"",0,0)
            }

        }
    }

}