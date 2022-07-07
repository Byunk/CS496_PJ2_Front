package com.example.cs496_pj2_front

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cs496_pj2_front.databinding.ActivityMainBinding
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .build()

        //val apiInterface = retrofit.create(APIInterface.class)

        // Binding Buttons
        val btnLogin = binding.btnLogin
        val btnKakaoLogin = binding.btnKakaoSignin

        // Button Click Listeners
        btnLogin.setOnClickListener {

            fun onClick() {
                val userID = binding.etId.toString()
                val userPW = binding.etPassword.toString()
            }
        }

        btnKakaoLogin.setOnClickListener {

            // Login (Kakao)
            signinWithKaKao()

        }


    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

            // Fetching User Data
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    var scopes = mutableListOf<String>()

                    if (user.kakaoAccount?.emailNeedsAgreement == true) { scopes.add("account_email") }
                    if (user.kakaoAccount?.birthdayNeedsAgreement == true) { scopes.add("birthday") }
                    if (user.kakaoAccount?.birthyearNeedsAgreement == true) { scopes.add("birthyear") }
                    if (user.kakaoAccount?.genderNeedsAgreement == true) { scopes.add("gender") }
                    if (user.kakaoAccount?.phoneNumberNeedsAgreement == true) { scopes.add("phone_number") }
                    if (user.kakaoAccount?.profileNeedsAgreement == true) { scopes.add("profile") }
                    if (user.kakaoAccount?.ageRangeNeedsAgreement == true) { scopes.add("age_range") }
                    if (user.kakaoAccount?.ciNeedsAgreement == true) { scopes.add("account_ci") }

                    if (scopes.count() > 0) {
                        Log.d(TAG, "사용자에게 추가 동의를 받아야 합니다.")

                        // OpenID Connect 사용 시
                        // scope 목록에 "openid" 문자열을 추가하고 요청해야 함
                        // 해당 문자열을 포함하지 않은 경우, ID 토큰이 재발급되지 않음
                        // scopes.add("openid")

                        //scope 목록을 전달하여 카카오 로그인 요청
                        UserApiClient.instance.loginWithNewScopes(this, scopes) { token, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 추가 동의 실패", error)
                            } else {
                                Log.d(TAG, "allowed scopes: ${token!!.scopes}")

                                // 사용자 정보 재요청
                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        Log.e(TAG, "사용자 정보 요청 실패", error)
                                    } else if (user != null) {
                                        Log.i(TAG, "사용자 정보 요청 성공" +
                                                "\n회원번호: ${user.id}" +
                                                "\n이메일: ${user.kakaoAccount?.email}" +
                                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun signinWithKaKao() {

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun logoutWithKaKao() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}