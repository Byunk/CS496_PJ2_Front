package com.example.cs496_pj2_front.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val name: String,

    @SerializedName("image_url")
    val imgUrl: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("food")
    var food: String?,

    @SerializedName("hobby")
    var hobby: String?,

    @SerializedName("favorites")
    var favorites: String?,

    @SerializedName("weekend")
    var weekend: String?,

    ): Parcelable

data class FriendsResponse(
    var friendsID: ArrayList<String> = arrayListOf<String>(),
)