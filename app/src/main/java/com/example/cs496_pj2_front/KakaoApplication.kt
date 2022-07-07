package com.example.cs496_pj2_front

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, R.string.kakao_native_key.toString())
    }

}