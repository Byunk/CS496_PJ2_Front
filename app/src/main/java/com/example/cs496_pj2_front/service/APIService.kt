package com.example.cs496_pj2_front.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {

    //private const val BASE_URL = "10.0.2.2:80" // For Testing
    private const val BASE_URL = "http://192.249.18.210" // For Testing

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitInterface: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }

    const val TAG = "APIService"
}