package com.example.isp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ispadmin.repository.ServerRepository
import com.example.ispadmin.viewmodel.ServerViewModel

class ServerViewModelFactory(private val serverRepository: ServerRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServerViewModel(serverRepository) as T
    }

}