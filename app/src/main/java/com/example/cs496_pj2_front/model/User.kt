package com.example.cs496_pj2_front.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class User(
    val id: UUID,

    val name: String,
    val imgUrl: String,
    var status: String = "",
    var friends: ArrayList<String> = arrayListOf<String>(),

    var food: String,
    var hobby: String,
    var favorites: String,
    var weekend: String,

    ): Parcelable