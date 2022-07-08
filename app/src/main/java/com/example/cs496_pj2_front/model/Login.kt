package com.example.cs496_pj2_front.model

import java.util.*

data class Login(
    val userId: UUID?
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