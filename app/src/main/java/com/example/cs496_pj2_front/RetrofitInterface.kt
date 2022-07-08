package com.example.cs496_pj2_front

import com.example.cs496_pj2_front.model.Login
import com.example.cs496_pj2_front.model.Schedule
import com.example.cs496_pj2_front.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface RetrofitInterface {

    @POST("/user/login")
    fun executeLogin(@Field("id") id: String, @Field("pw") pw: String): Call<Login>

    @POST("/user/login")
    fun executeLogin(@Field("kakaoId") kakaoId: Long): Call<Login>


    // if kakaoId exists, username should be automatically fetched from KakaoTalk
    @POST("/user/signup")
    fun executeSignup(@Field("id") id: String, @Field("pw") pw: String, @Field("username") username: String): Call<Int>


    @POST("/user/signup")
    fun executeSignup(@Field("id") id: String, @Field("pw") pw: String, @Field("username") username: String, @Field("kakaoId") kakaoId: Long?): Call<Int>

    @GET("/user/{id}")
    fun getUserById(@Query("id") id: UUID): Call<User>

    @GET("/")
    fun getUserSchedule(@Query("year") year: Int, @Query("month") month: Int): Call<Schedule>

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