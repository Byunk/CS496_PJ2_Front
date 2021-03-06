package com.example.cs496_pj2_front.service

import com.example.cs496_pj2_front.model.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface RetrofitInterface {

    @POST("/user/login")
    fun executeLogin(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/user/signup")
    fun executeSignup(@Body signupRequest: SignupRequest): Call<ResponseCode>

    // id: userID
    @GET("/user/{id}")
    fun getUserById(@Path("id") id: String): Call<User>

    @GET("/")
    fun getUserFriends(@Path("id") id: String): Call<Friends>

    // id: userID
    @GET("/")
    fun getUserMonthlySchedule(@Path("id") id: String, @Query("year") year: Int, @Query("month") month: Int): Call<ArrayList<Schedule>>

    @GET()
    fun getUserDailySchedule(@Path("id") id: String, @Query("year") year: Int, @Query("month") month: Int, @Query("date") date: Int): Call<ArrayList<Schedule>>
/*
    @GET("/test")
    fun test(): Call<Int>*/
}

const val SUCCESS = 200
const val FAILURE = 400

data class ResponseCode(
    val code: Int = 400
)
