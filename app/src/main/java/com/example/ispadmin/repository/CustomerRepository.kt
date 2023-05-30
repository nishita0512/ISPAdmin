package com.example.ispadmin.repository


import com.example.ispadmin.api.RetrofitInstance
import com.example.ispadmin.model.Customer
import okhttp3.RequestBody
import okhttp3.ResponseBody

class CustomerRepository {

    suspend fun getCustomerById(custId: Int): Customer {
        return RetrofitInstance.custApi.getCustomerById(custId)
    }

    suspend fun getAllCustomers(): ArrayList<Customer>{
        return RetrofitInstance.custApi.getAllCustomers()
    }

    suspend fun addCustomer(fields: HashMap<String, RequestBody>): Customer{
        return RetrofitInstance.custApi.addCustomer(fields)
    }

    suspend fun updateCustomer(fields: HashMap<String, RequestBody>): ResponseBody{
        return RetrofitInstance.custApi.updateCustomer(fields)
    }

}