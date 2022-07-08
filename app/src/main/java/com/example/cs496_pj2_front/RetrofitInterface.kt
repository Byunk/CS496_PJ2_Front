package com.example.cs496_pj2_front

import com.example.cs496_pj2_front.model.Login
import com.example.cs496_pj2_front.model.Schedule
import com.example.cs496_pj2_front.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

interface RetrofitInterface {

    @POST("/user/login")
    fun executeLogin(id: String, pw: String): Call<Login>

    @POST("/user/login")
    fun executeLogin(kakaoId: Long): Call<Login>

    // if kakaoId exists, username should be automatically fetched from KakaoTalk
    @POST("/user/signup")
    fun executeSignup(id: String, pw: String, username: String): Call<Int>

    @POST("/user/signup")
    fun executeSignup(id: String, pw: String, username: String, kakaoId: Long): Call<Int>

    @GET("/user/{id}")
    fun getUserById(id: UUID): Call<User>

    @GET("/")
    fun getUserSchedule(year: Int, month: Int): Call<Schedule>

    @GET("/test")
    fun test(): Call<Int>
}

const val SUCCESS = 200
const val FAILURE = 400


/*
enum class ResponseCode(val code: Int) {
    SUCCESS(200),
    FAILURE(400)
}*/