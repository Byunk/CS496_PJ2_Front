package com.example.cs496_pj2_front.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: String?
)

data class LoginRequest(
    val id: String?,
    val pw: String?,
    val kakaoid: String?
)

data class SignupRequest(
    val id: String,
    val pw: String,
    val username: String,
    val kakaoid: String?
)