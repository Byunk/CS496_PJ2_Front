package com.example.cs496_pj2_front.model

import com.example.cs496_pj2_front.ResponseCode
import java.io.Serializable

data class User(
    val code: ResponseCode,

    // Login Info
    val id: String,
    val pw: String,
    val username: String,
    val kakaoId: Long?,

    // Variables
    var status: String = "",
    var friends: ArrayList<String> = arrayListOf<String>()
): Serializable

object UserData