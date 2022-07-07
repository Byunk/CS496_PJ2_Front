package com.example.cs496_pj2_front.model

data class Profile(
    val id: String,
    var status: String,
    var image: String
    )

data class ProfileResponse(
    val users: List<Profile>
)