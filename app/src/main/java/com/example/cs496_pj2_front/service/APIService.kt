package com.example.cs496_pj2_front.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {

    //private const val BASE_URL = "172.10.5.155"
    //private const val BASE_URL = "172.10.5.160"
    //private const val BASE_URL = "10.0.2.2:80" // For Testing
    private const val BASE_URL = "http://143.248.167.44:88" // For Testing

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitInterface: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }
}