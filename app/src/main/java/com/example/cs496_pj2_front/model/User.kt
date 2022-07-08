package com.example.cs496_pj2_front.model

import android.os.Parcel
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
    var friends: ArrayList<UUID> = arrayListOf<UUID>(),

    var food: String,
    var hobby: String,
    var favorites: String,
    var weekend: String,

    ): Parcelable

/*
fun createUserWithKakaoAccount(kakaoId: Long, account: com.kakao.sdk.user.model.Account): User {
    val id = account.email!!
    val username = account.profile?.nickname!!

    //return User(id = id, username = username, kakaoId = kakaoId)
}*/