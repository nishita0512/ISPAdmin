package com.example.ispadmin.api

import com.example.ispadmin.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val custApi: CustomerApi by lazy{
        retrofit.create(CustomerApi::class.java)
    }

    val serverApi: ServerApi by lazy{
        retrofit.create(ServerApi::class.java)
    }

}