package com.example.cs496_pj2_front

import com.example.cs496_pj2_front.model.Login
import com.example.cs496_pj2_front.model.Profile
import com.example.cs496_pj2_front.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitInterface {

    @GET("/user/")
    fun getUserList(): Call<Profile>

    @POST("/user/login")
    fun executeLogin(id: String, pw: String): Call<User>

    @POST("/user/signup")
    fun executeSignup(id: String, pw: String, username: String, kakaoId: Long?): Call<ResponseCode>

    @GET("/user/{id}")
    fun getUserByKakao(kakaoId: Long): Call<User>

    @GET("/test")
    fun test(): Call<ResponseCode>
}

data class Response(
    val code: ResponseCode
)

enum class ResponseCode(val code: Int) {
    SUCCESS(200),
    FAILURE(400)
}