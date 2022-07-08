package com.example.cs496_pj2_front

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Toast
import com.example.cs496_pj2_front.databinding.ActivityLoginBinding
import com.example.cs496_pj2_front.model.Login
import com.example.cs496_pj2_front.model.User
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val context = this

    lateinit var data: User
    lateinit var kakaoData: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hash Key
        //val keyHash = Utility.getKeyHash(this)
        //Log.i(TAG, "Hash Key: " + keyHash)

        // Binding Buttons
        val btnLogin = binding.btnLogin
        val btnSignup = binding.btnSignup
        val btnKakaoLogin = binding.btnKakaoSignin

        // Login
        btnLogin.setOnClickListener {
            val id = binding.etId.toString()
            val pw = binding.etPassword.toString()
            executeLogin(id, pw)
        }

        // Signup
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Kakao Login
        // Check Login Token Info
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                }
                else if (tokenInfo != null) {
                    Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                    executeKakaoLogin()
                }
            }
        }

        // Button Click Listeners
        btnKakaoLogin.setOnClickListener {
            //Toast.makeText(this, "로그인 버튼 클릭 이벤트.", Toast.LENGTH_SHORT).show()
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    executeKakaoLogin()
                }
            }

            signinWithKaKao(callback)
        }
    }

    private fun intentToMain(data: User) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("userData", data)
        startActivity(intent)
        finish()
    }

    private fun executeLogin(id: String, pw: String) {

        val call = APIService.retrofitInterface.executeLogin(id, pw)
        call.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, t.message!!)
                Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body()?.code == ResponseCode.FAILURE) {
                    // Move to sign up page
                    Toast.makeText(context, "계정정보가 존재하지 않음.", Toast.LENGTH_SHORT).show()
                } else {
                    // Move to Main Activity
                    data = response.body()!!
                    intentToMain(data)
                }
            }
        })

    }

    private fun executeKakaoLogin() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 요청 정보 실패", error)
            } else if (user != null) {
                val id = user.id!!

                // Fetch KakaoTalk Data and POST
                kakaoData = user.kakaoAccount!!

                Log.i(TAG, "사용자 요청 정보 성공" +
                        "\n회원번호: ${id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.profileImageUrl}")

                // Check DB
                val call = APIService.retrofitInterface.getUserByKakao(id)
                call.enqueue(object: Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e(TAG, t.message!!)
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val response = response.body()!!
                        if (response.code == ResponseCode.FAILURE) {
                            // If no local ID, PW -> Auto Sign up
                            val callSignup = APIService.retrofitInterface.executeSignup(user.kakaoAccount?.email!!, "password", user.kakaoAccount?.profile?.nickname!!, id)

                            callSignup.enqueue(object: Callback<ResponseCode>{
                                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                                    Log.e(TAG, t.message!!)
                                    Toast.makeText(context, "자동 로그인 오류. 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(
                                    call: Call<ResponseCode>,
                                    response: Response<ResponseCode>
                                ) {
                                   if (response.body() == ResponseCode.FAILURE) {
                                       Toast.makeText(context, "회원 가입 실패.", Toast.LENGTH_SHORT).show()
                                   } else {
                                       Toast.makeText(context, "자동 로그인 성공. 비밀번호를 변경하세요. 기본 비밀번호: password", Toast.LENGTH_SHORT).show()

                                       // Make Data
                                       data = User(ResponseCode.SUCCESS, kakaoData.email!!, "password", kakaoData.profile?.nickname!!, id)

                                       intentToMain(data)
                                   }
                                }
                            })
                        } else {
                            // If exists, Sign in
                            data = response
                            intentToMain(data)
                        }
                    }
                })
            }
        }
    }

    private fun signinWithKaKao(callback: (OAuthToken?, Throwable?) -> Unit) {
        // Login (Kakao)
        Log.i(TAG, "Kakao Login Event")
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
                    executeKakaoLogin()
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