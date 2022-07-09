package com.example.cs496_pj2_front.model

data class Login(
    val userId: String
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