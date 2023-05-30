package com.example.ispadmin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ispadmin.repository.CustomerRepository

class CustomerViewModelFactory(private val customerRepository: CustomerRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CustomerViewModel(customerRepository) as T
    }

}