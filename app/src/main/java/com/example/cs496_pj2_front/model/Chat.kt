package com.example.cs496_pj2_front.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val title: String,
    val users: ArrayList<String>,
    var chatLog: ArrayList<String>

    ): Parcelable