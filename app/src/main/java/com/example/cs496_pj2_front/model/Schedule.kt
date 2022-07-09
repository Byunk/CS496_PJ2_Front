package com.example.cs496_pj2_front.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Schedule(
    val year: Int,
    val month: Int,
    val date: Int,
    val hour: Int,
    val minute: Int,

    val todo: String = "",
    val location: String = "",
    val friends: ArrayList<String> = arrayListOf()
): Parcelable