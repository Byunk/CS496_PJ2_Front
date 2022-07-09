package com.example.cs496_pj2_front.service

import com.example.cs496_pj2_front.model.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface RetrofitInterface {

    @POST("/user/login")
    fun executeLogin(@Body loginRequest: LoginRequest): Call<Login>

    @POST("/user/signup")
    fun executeSignup(@Body signupRequest: SignupRequest): Call<Int>

    @GET("/user/{id}")
    fun getUserById(@Path("id") id: String): Call<User>

    @GET("/")
    fun getUserSchedule(@Query("year") year: Int, @Query("month") month: Int): Call<Schedule>

    @GET("/test")
    fun test(): Call<Int>
}

const val SUCCESS = 200
const val FAILURE = 400
