package com.example.ispadmin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ispadmin.model.Customer
import com.example.ispadmin.repository.CustomerRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class CustomerViewModel(private val customerRepository: CustomerRepository): ViewModel() {

    val myResponse: MutableLiveData<Customer> = MutableLiveData()
    val myResponseList: MutableLiveData<ArrayList<Customer>> = MutableLiveData()
    val myResponseString: MutableLiveData<String> = MutableLiveData()

    fun getCustomerById(custId: Int){
        viewModelScope.launch {
            try{
                val response = customerRepository.getCustomerById(custId)
                myResponse.value = response
            }
            catch(e: Exception){
                myResponse.value = Customer(-1,"","","",0.00,0.00,"","",0,0,0,0,0)
            }

        }
    }

    fun getAllCustomers(){
        viewModelScope.launch {
            try{
                val response = customerRepository.getAllCustomers()
                myResponseList.value = response
            }
            catch(e: Exception){
                myResponseList.value = ArrayList()
            }

        }
    }

    fun addCustomer(fields: HashMap<String, RequestBody>){
        viewModelScope.launch {
            try{
                val response = customerRepository.addCustomer(fields)
                myResponse.value = response
            }
            catch(e: Exception){
                myResponse.value = Customer(-1,"","","",0.00,0.00,"","",0,0,0,0,0)
            }
        }
    }

    fun updateCustomer(fields: HashMap<String, RequestBody>){
        viewModelScope.launch {
            try{
                val response = customerRepository.updateCustomer(fields)
                myResponseString.value = response.string()
            }
            catch(e: Exception){
                myResponseString.value = "Failed"
            }
        }
    }

}