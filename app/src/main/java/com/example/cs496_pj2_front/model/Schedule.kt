package com.example.cs496_pj2_front.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Schedule(
    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("date")
    val date: Int,
    @SerializedName("time")
    val time: Int,

    @SerializedName("todo")
    val todo: String?,

    @SerializedName("location")
    val location: String?,

    @SerializedName("friends")
    val friends: ArrayList<String>?
): Parcelable